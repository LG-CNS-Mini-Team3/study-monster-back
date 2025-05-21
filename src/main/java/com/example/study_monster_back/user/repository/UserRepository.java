package com.example.study_monster_back.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.study_monster_back.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByEmail(String email);//email로 유저 찾는 로직
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
