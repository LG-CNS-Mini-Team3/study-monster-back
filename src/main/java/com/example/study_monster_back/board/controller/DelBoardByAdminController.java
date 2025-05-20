package com.example.study_monster_back.board.controller;

import com.example.study_monster_back.board.service.DelBoardByAdminService;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.study_monster_back.board.dto.request.DelBoardRequest;
import com.example.study_monster_back.board.dto.response.BoardResponse;
import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.board.repository.BoardRepository;

@RestController
@RequestMapping("/boards")
public class DelBoardByAdminController {
    private final DelBoardByAdminService delBoardByAdminService;
    private final BoardRepository boardRepository;

    public DelBoardByAdminController(DelBoardByAdminService delBoardByAdminService, BoardRepository boardRepository) {
        this.delBoardByAdminService = delBoardByAdminService;
        this.boardRepository = boardRepository;
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getBoardsForAdmin(@RequestParam Long adminId) {
        try {
            delBoardByAdminService.isAdmin(adminId);
            List<Board> boards = boardRepository.findAll();
            List<BoardResponse> result = boards.stream()
                    .map(BoardResponse::from)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(result);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "관리자 권한이 없습니다."));
        }
    }

    @PostMapping("/admin/delete")
    public ResponseEntity<?> deleteBoardByAdmin(@RequestBody DelBoardRequest request) {
        try {
            delBoardByAdminService.deleteBoardByAdmin(request.getBoardId(), request.getAdminId());
            return ResponseEntity.ok("게시글 삭제 완료");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자 권한이 없습니다.");
        }
    }
}
