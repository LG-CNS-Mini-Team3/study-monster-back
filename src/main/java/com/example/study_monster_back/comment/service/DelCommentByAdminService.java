package com.example.study_monster_back.comment.service;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.study_monster_back.comment.entity.Comment;
import com.example.study_monster_back.comment.repository.CommentRepository;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DelCommentByAdminService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;

    public DelCommentByAdminService(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public void isAdmin(Long adminId) throws AccessDeniedException {
        User admin = userRepository
                .findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("관리자 계정을 찾을 수 없습니다."));

        if (!"ADMIN".equalsIgnoreCase(admin.getRole())) {
            throw new AccessDeniedException("관리자 권한이 없습니다.");
        }
    }

    public void deleteCommentByAdmin(Long commentId, Long adminId) throws AccessDeniedException {
        isAdmin(adminId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("삭제할 댓글이 존재하지 않습니다."));
        commentRepository.delete(comment);
    }

}
