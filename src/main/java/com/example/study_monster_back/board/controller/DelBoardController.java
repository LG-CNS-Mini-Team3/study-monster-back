package com.example.study_monster_back.board.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.study_monster_back.board.dto.request.BoardRequest;
import com.example.study_monster_back.board.dto.reponse.BoardResponse;
import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.board.repository.BoardRepository;
import com.example.study_monster_back.board.service.DelBoardService;

@RestController
@RequestMapping("/boards")
public class DelBoardController {

    private final DelBoardService delBoardService;
    private final BoardRepository boardRepository;

    public DelBoardController(DelBoardService delBoardService, BoardRepository boardRepository) {
        this.delBoardService = delBoardService;
        this.boardRepository = boardRepository;
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getBoardsForAdmin(@RequestParam Long userId) {
        try {
            delBoardService.isAdmin(userId);

            List<Board> boards = boardRepository.findAll();
            List<BoardResponse> response = boards.stream()
                    .map(BoardResponse::from)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).body("관리자 권한이 없습니다.");
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteBoard(@RequestBody BoardRequest request) {
        try {
            delBoardService.deleteBoard(request.getBoardId(), request.getUserId());
            return ResponseEntity.ok("게시글이 삭제되었습니다.");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).body("삭제 권한이 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시글 삭제 중 오류가 발생했습니다.");
        }
    }
}