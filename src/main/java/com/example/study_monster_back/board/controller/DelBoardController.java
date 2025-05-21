package com.example.study_monster_back.board.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import com.example.study_monster_back.board.dto.request.BoardRequest;
import com.example.study_monster_back.board.dto.response.BoardResponse;
import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.board.repository.BoardRepository;
import com.example.study_monster_back.board.service.DelBoardService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
public class DelBoardController {
    private final DelBoardService delBoardService;
    private final BoardRepository boardRepository;

    public DelBoardController(DelBoardService delBoardService, BoardRepository boardRepository) {
        this.delBoardService = delBoardService;
        this.boardRepository = boardRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        List<BoardResponse> result = boards.stream()
                .map(BoardResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteBoard(@RequestBody BoardRequest request) {
        try {
            delBoardService.deleteBoard(request.getBoardId(), request.getUserId());
            return ResponseEntity.ok("게시글이 삭제되었습니다.");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }
    }
}
