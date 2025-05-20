package com.example.study_monster_back.comment.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.study_monster_back.comment.dto.request.DelCommentRequest;
import com.example.study_monster_back.comment.dto.response.CommentResponse;
import com.example.study_monster_back.comment.entity.Comment;
import com.example.study_monster_back.comment.repository.CommentRepository;
import com.example.study_monster_back.comment.service.DelCommentByAdminService;

@RestController
@RequestMapping("/admin/comments")
public class DelCommentByAdminController {
    @Autowired
    DelCommentByAdminService delCommentByAdminService;
    @Autowired
    CommentRepository commentRepository;

    @GetMapping
    public ResponseEntity<?> getAllComments(@RequestParam Long adminId) throws AccessDeniedException {
        delCommentByAdminService.isAdmin(adminId);

        List<Comment> comments = commentRepository.findAll();
        List<CommentResponse> response = comments.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/comments/delete")
    public ResponseEntity<?> deleteCommentByAdmin(
            @RequestBody DelCommentRequest request) throws AccessDeniedException {
        delCommentByAdminService.deleteCommentByAdmin(request.getCommentId(), request.getAdminId());
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}
