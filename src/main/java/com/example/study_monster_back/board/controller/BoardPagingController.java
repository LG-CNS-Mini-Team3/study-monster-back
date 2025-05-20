package com.example.study_monster_back.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.study_monster_back.board.dto.response.BoardResponse;
import com.example.study_monster_back.board.service.BoardPagingService;

@RestController
@RequestMapping("/boards")
public class BoardPagingController {
    @Autowired
    BoardPagingService boardPagingService;

    @GetMapping
    public Page<BoardResponse> boardList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created_at").descending());
        return boardPagingService.getBoards(search, pageable);
    }
}
