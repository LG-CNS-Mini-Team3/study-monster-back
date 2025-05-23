package com.example.study_monster_back.board.service;

import com.example.study_monster_back.board.dto.response.BoardResponse;
import com.example.study_monster_back.board.dto.response.BoardWithTagsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardSearchService {

    Page<BoardResponse> getBoards(String keyword, String type, Pageable pageable);

    Page<BoardWithTagsResponseDto> getBoardsByTags(List<String> tags, Pageable pageable);
}
