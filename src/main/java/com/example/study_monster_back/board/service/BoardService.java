package com.example.study_monster_back.board.service;

import com.example.study_monster_back.board.dto.request.CreateBoardRequestDto;
import com.example.study_monster_back.board.dto.response.CreateBoardResponseDto;

public interface BoardService {

    CreateBoardResponseDto createBoard(CreateBoardRequestDto boardRequestDto);
}
