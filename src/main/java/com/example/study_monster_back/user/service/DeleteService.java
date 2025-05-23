package com.example.study_monster_back.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class DeleteService {
    private final UserRepository userRepository;

    public void signOut(String Email) {
        Optional<User> user = userRepository.findByEmail(Email);
        if (user.isPresent()) {
            userRepository.deleteById(user.get().getId());
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
