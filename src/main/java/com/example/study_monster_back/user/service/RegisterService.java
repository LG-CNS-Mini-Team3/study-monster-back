package com.example.study_monster_back.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.study_monster_back.user.dto.request.RegisterRequestDto;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Slf4j
@Service
public class RegisterService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void RegisterUser(RegisterRequestDto registerRequestDto) {
        if (userRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        if (userRepository.existsByNickname(registerRequestDto.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
        User user = new User();
        user.setEmail(registerRequestDto.getEmail());
        user.setName(registerRequestDto.getName());
        user.setNickname(registerRequestDto.getNickname());
        user.setPhone_number(registerRequestDto.getPhone_number());
        String rawPwd = registerRequestDto.getOriginPwd();
        String checkPwd = registerRequestDto.getCheckPwd();
        if (rawPwd.equals(checkPwd)) {
            String encryptPwd = bCryptPasswordEncoder.encode(rawPwd);
            user.setPwd(encryptPwd);
        }
        else{
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않거나 유효하지 않은 비밀번호입니다.");
        }
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }
}
