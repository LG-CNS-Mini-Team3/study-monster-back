package com.example.study_monster_back.board.controller;

import java.nio.file.AccessDeniedException;
import java.util.Map;

import com.example.study_monster_back.board.dto.response.BoardResponse;
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

    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoardById(@PathVariable Long boardId) {
        return boardRepository.findById(boardId)
                .map(board -> ResponseEntity.ok(BoardResponse.from(board)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteBoard(@RequestBody Map<String, Long> request) {
        Long boardId = request.get("boardId");
        Long userId = request.get("userId");
        try {
            delBoardService.deleteBoard(boardId, userId);
            return ResponseEntity.ok("게시글이 삭제되었습니다.");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }
    }
}
