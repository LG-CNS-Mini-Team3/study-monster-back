package com.example.study_monster_back.board.dto.response;

import com.example.study_monster_back.board.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetBoardResponseDto {
    private Long boardId;
    private String title;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private Long userId;
    private String email;
    private String nickname;

    @Builder
    private GetBoardResponseDto(Board board) {
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.created_at = board.getCreated_at();
        this.updated_at = board.getUpdated_at();
        this.userId = board.getUser().getId();
        this.email = board.getUser().getEmail();
        this.nickname = board.getUser().getNickname();
    }
}
