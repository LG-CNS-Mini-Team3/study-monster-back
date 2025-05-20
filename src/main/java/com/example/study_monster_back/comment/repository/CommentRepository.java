package com.example.study_monster_back.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.study_monster_back.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
