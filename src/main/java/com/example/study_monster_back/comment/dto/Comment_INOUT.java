package com.example.study_monster_back.comment.dto;

import java.time.format.DateTimeFormatter;

import com.example.study_monster_back.comment.entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment_INOUT {
    private Long id;
    private Long user_id;
    private Long board_id;
    private String content;
    private String created_at;
    private String updated_at;
    private String username;

    public Comment_INOUT(Comment comment){
        this.id = comment.getId();
        this.user_id = comment.getUser() != null ? comment.getUser().getId() : null;
        this.board_id = comment.getBoard() != null ? comment.getBoard().getId() : null;
        this.content = comment.getContent();
        this.created_at = comment.getCreated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.updated_at = comment.getUpdated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.username = comment.getUser() != null ? comment.getUser().getNickname() : null;
    }
}
