package com.example.study_monster_back.comment.service;

import java.nio.file.AccessDeniedException;

import org.springframework.stereotype.Service;

import com.example.study_monster_back.comment.entity.Comment;
import com.example.study_monster_back.comment.repository.CommentRepository;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DelCommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public DelCommentService(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public void deleteComment(Long commentId, Long requestUserId) throws AccessDeniedException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        User requestUser = userRepository.findById(requestUserId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 정보를 찾을 수 없습니다."));

        boolean isAdmin = "ADMIN".equalsIgnoreCase(requestUser.getRole());
        boolean isWriter = comment.getUser().getId().equals(requestUserId);

        if (!isAdmin && !isWriter) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}
