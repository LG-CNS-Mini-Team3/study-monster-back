package com.example.study_monster_back.board.controller;

import com.example.study_monster_back.board.dto.response.GetBoardResponseDto;
import com.example.study_monster_back.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/{boardId}")
    public ResponseEntity<GetBoardResponseDto> getBoard(
        @PathVariable(value = "boardId") Long boardId
    ) {
        return ResponseEntity.ok(boardService.getBoard(boardId));
    }
}
