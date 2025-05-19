package com.example.study_monster_back.user.respository;

import com.example.study_monster_back.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
