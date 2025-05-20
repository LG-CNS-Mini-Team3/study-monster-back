package com.example.study_monster_back.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.study_monster_back.board.dto.response.BoardResponse;
import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.board.repository.BoardRepository;

@Service
public class BoardPagingService {
    @Autowired
    BoardRepository boardsearchRepository;

    // 페이징 + 검색
    public Page<BoardResponse> getBoards(String keyword, Pageable pageable) {
        Page<Board> boards = boardsearchRepository.searchByKeyword(keyword, pageable);
        return boards.map(BoardResponse::from);
    }
}
