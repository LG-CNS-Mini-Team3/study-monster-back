package com.example.study_monster_back.user.repository;
import java.util.Optional;

@Repository
    Optional<User> findByEmail(String email);//email로 유저 찾는 로직
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
