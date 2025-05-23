package com.example.study_monster_back.user.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.study_monster_back.user.dto.request.UpdateRequestDto;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class UserUpdate {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User updateUser(String email, UpdateRequestDto updateRequestDto) {
        // 사용자 정보 추출
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 비밀번호
        if (updateRequestDto.getOldPwd() != null && !updateRequestDto.getOldPwd().trim().isEmpty()) {

            if (updateRequestDto.getNewPwd() == null || updateRequestDto.getNewPwd().trim().isEmpty()) {
                throw new IllegalArgumentException("기존 비밀번호를 입력해야합니다.");
            }

            boolean matches = passwordEncoder.matches(updateRequestDto.getOldPwd(), user.getPwd());

            if (!matches) {
                throw new BadCredentialsException("기존 비밀번호가 일치하지 않습니다.");
            }
            user.setPwd(passwordEncoder.encode(updateRequestDto.getNewPwd()));
        }
        // 이름
        if (updateRequestDto.getName() != null) {
            user.setName(updateRequestDto.getName());
        }
        // 닉네임
        if (updateRequestDto.getNickname() != null) {
            user.setNickname(updateRequestDto.getNickname());
        }
        return userRepository.save(user);
    }
}
