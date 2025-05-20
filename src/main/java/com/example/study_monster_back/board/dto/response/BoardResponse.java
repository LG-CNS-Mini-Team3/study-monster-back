package com.example.study_monster_back.board.dto.response;

import java.time.LocalDateTime;

import com.example.study_monster_back.board.entity.Board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponse {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdAt;

    public static BoardResponse from(Board board) {
        return new BoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getUser().getNickname(),
                board.getCreated_at());
    }
}
