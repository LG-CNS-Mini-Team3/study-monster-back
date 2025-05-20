package com.example.study_monster_back.board.dto.request;

import lombok.Data;

@Data
public class DelBoardRequest {
    private Long boardId;
    private Long adminId;
}
