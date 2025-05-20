package com.example.study_monster_back.board.service;

import com.example.study_monster_back.board.dto.request.UpdateBoardRequestDto;
import com.example.study_monster_back.board.dto.response.GetBoardResponseDto;
import com.example.study_monster_back.board.dto.request.CreateBoardRequestDto;
import com.example.study_monster_back.board.dto.response.CreateBoardResponseDto;
import com.example.study_monster_back.board.dto.response.UpdateBoardResponseDto;

public interface BoardService {

    GetBoardResponseDto getBoard(Long boardId);

    CreateBoardResponseDto createBoard(CreateBoardRequestDto boardRequestDto);

    UpdateBoardResponseDto updateBoard(Long boardId, UpdateBoardRequestDto boardRequestDto);

    void deleteBoard(Long boardId);
}
