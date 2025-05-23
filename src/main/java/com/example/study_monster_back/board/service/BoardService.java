package com.example.study_monster_back.board.service;

import com.example.study_monster_back.board.dto.request.UpdateBoardRequestDto;
import com.example.study_monster_back.board.dto.response.GetBoardResponseDto;
import com.example.study_monster_back.board.dto.request.CreateBoardRequestDto;
import com.example.study_monster_back.board.dto.response.CreateBoardResponseDto;
import com.example.study_monster_back.board.dto.response.UpdateBoardResponseDto;
import com.example.study_monster_back.tag.dto.response.TagResponseDto;
import com.example.study_monster_back.board.dto.response.StudyFeedbackResponse;

import java.util.List;

public interface BoardService {

    StudyFeedbackResponse getStudyFeedback(Long boardId);

    GetBoardResponseDto getBoard(Long boardId);

    CreateBoardResponseDto createBoard(CreateBoardRequestDto boardRequestDto, String email);

    UpdateBoardResponseDto updateBoard(Long boardId, UpdateBoardRequestDto boardRequestDto, String email);

    void deleteBoard(Long boardId, String email);

    List<TagResponseDto> getBoardTags(Long boardId);
}
