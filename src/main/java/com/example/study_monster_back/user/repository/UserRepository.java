package com.example.study_monster_back.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.study_monster_back.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
