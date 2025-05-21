package com.example.study_monster_back.board.service;

import com.example.study_monster_back.board.dto.request.UpdateBoardRequestDto;
import com.example.study_monster_back.board.dto.response.GetBoardResponseDto;
import com.example.study_monster_back.board.dto.request.CreateBoardRequestDto;
import com.example.study_monster_back.board.dto.response.CreateBoardResponseDto;
import com.example.study_monster_back.board.dto.response.UpdateBoardResponseDto;
import com.example.study_monster_back.tag.dto.response.TagResponseDto;

import java.util.List;

public interface BoardService {

    GetBoardResponseDto getBoard(Long boardId);

    CreateBoardResponseDto createBoard(CreateBoardRequestDto boardRequestDto);

    UpdateBoardResponseDto updateBoard(Long boardId, UpdateBoardRequestDto boardRequestDto);

    void deleteBoard(Long boardId);

    List<TagResponseDto> getBoardTags(Long boardId);
}
