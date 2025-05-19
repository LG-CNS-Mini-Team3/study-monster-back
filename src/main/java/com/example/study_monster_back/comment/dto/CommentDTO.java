package com.example.study_monster_back.comment.dto;

import com.example.study_monster_back.comment.entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private String created_at;
    private String updated_at;
    private String username;

    public CommentDTO(Comment comment){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.created_at = comment.getCreated_at().toString();
        this.updated_at = comment.getUpdated_at().toString();
        this.username = comment.getUser() != null ? comment.getUser().getName() : null;
    }
}
