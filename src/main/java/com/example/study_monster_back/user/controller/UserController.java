package com.example.study_monster_back.user.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.study_monster_back.jwt.JwtTokenProvider;
import com.example.study_monster_back.user.dto.request.LoginRequestDto;
import com.example.study_monster_back.user.dto.request.RegisterRequestDto;
import com.example.study_monster_back.user.dto.response.LoginResponseDto;
import com.example.study_monster_back.user.dto.response.RegisterResponseDto;
import com.example.study_monster_back.user.dto.response.UpdateResponseDto;
import com.example.study_monster_back.user.dto.response.UserInfoResponseDto;
import com.example.study_monster_back.user.service.DeleteService;
import com.example.study_monster_back.user.service.LoginService;
import com.example.study_monster_back.user.service.RegisterService;
import com.example.study_monster_back.user.service.UserService;
import com.example.study_monster_back.user.service.UserUpdate;
import com.example.study_monster_back.user.dto.request.UpdateRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final RegisterService registerService;
    private final LoginService loginService;
    private final UserService userService;
    private final UserUpdate userUpdate;
    private final DeleteService deleteService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
        log.info("컨트롤러 실행");
        registerService.RegisterUser(registerRequestDto);
        RegisterResponseDto registerResponseDto = new RegisterResponseDto("회원가입이 성공적으로 완료되었습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getEmail(),
                            loginRequestDto.getPwd()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = jwtTokenProvider.createToken(authentication);
            LoginResponseDto loginResponseDto = new LoginResponseDto("로그인 성공 and Token생성 성공!", jwtToken);
            return ResponseEntity.ok(loginResponseDto);
        } catch (AuthenticationException e) {
            log.info("로그인 실패!" + e.getMessage());
            LoginResponseDto loginResponseDto = new LoginResponseDto(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponseDto);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<UserInfoResponseDto> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            log.warn("인증되지 않은 사용자의 정보 조회 시도");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String email = authentication.getName();
        try {
            UserInfoResponseDto userInfo = userService.getUserInfoByEmail(email);
            return ResponseEntity.ok(userInfo);
        } catch (IllegalArgumentException e) {
            log.warn("사용자 정보를 찾을 수 없습니다 (getUserInfo): {}", email, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new UserInfoResponseDto(null, e.getMessage(), null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("로그아웃 완료");
    }

    @PutMapping("/update")
    public ResponseEntity<UpdateResponseDto> updateUser(@RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UpdateRequestDto updateRequestDto) {
        // ContextHolder로 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            log.warn("인증되지 않은 사용자의 회원 정보 수정 시도");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        // principal추출
        String email = authentication.getName();
        try {
            com.example.study_monster_back.user.entity.User updatedUser = userUpdate.updateUser(email,
                    updateRequestDto);
            // 서비스 계층이 리턴한 updatedUser를 responseDto에 담아서 응답
            UpdateResponseDto updateResponseDto = new UpdateResponseDto();
            updateResponseDto.setEmail(updatedUser.getEmail());
            updateResponseDto.setName(updatedUser.getName());
            updateResponseDto.setNickname(updatedUser.getNickname());
            return ResponseEntity.ok(updateResponseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/signout")
    public ResponseEntity<?> signOut(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        String email = jwtTokenProvider.extractEmail(jwtToken);
        deleteService.signOut(email);
        return ResponseEntity.ok("회원탈퇴 완료");
    }
}
