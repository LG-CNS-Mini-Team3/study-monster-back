package com.example.study_monster_back.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.study_monster_back.comment.entity.Comment;
import com.example.study_monster_back.user.entity.User;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findByBoardId(Long boardId);
    Comment findByIdAndUser(Long id, User user);
}
