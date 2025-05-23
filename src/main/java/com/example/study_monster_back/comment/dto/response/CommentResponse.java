package com.example.study_monster_back.comment.dto.response;

import java.time.LocalDateTime;

import com.example.study_monster_back.comment.entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getNickname(),
                comment.getCreated_at());
    }
}
