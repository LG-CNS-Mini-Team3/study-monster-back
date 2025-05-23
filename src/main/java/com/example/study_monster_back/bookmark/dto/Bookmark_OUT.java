package com.example.study_monster_back.bookmark.dto;

import com.example.study_monster_back.board.entity.Board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark_OUT {
    private Long board_id;
    private String content;
    private String title;
    private String username;

    public Bookmark_OUT(Board board) {
        this.board_id = board.getId();
        this.content = board.getContent();
        this.title = board.getTitle();
        this.username = board.getUser().getNickname();
    }
}
