package com.example.study_monster_back.comment.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.study_monster_back.comment.dto.request.CommentRequest;
import com.example.study_monster_back.comment.dto.response.CommentResponse;
import com.example.study_monster_back.comment.entity.Comment;
import com.example.study_monster_back.comment.repository.CommentRepository;
import com.example.study_monster_back.comment.service.DelCommentService;

@RestController
@RequestMapping("/comments")
public class DelCommentController {

    private final DelCommentService delCommentService;
    private final CommentRepository commentRepository;

    public DelCommentController(DelCommentService delCommentService, CommentRepository commentRepository) {
        this.delCommentService = delCommentService;
        this.commentRepository = commentRepository;
    }

    @GetMapping
    public ResponseEntity<?> getCommentsByBoard(@RequestParam Long boardId) {
        List<Comment> comments = commentRepository.findByBoardId(boardId);
        List<CommentResponse> response = comments.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteComment(@RequestBody CommentRequest request) {
        try {
            delCommentService.deleteComment(request.getCommentId(), request.getUserId());
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).body("삭제 권한이 없습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("댓글이 존재하지 않습니다.");
        }
    }
}
