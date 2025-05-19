package com.example.study_monster_back.board.controller;

import com.example.study_monster_back.board.dto.response.StudyFeedbackResponse;
import com.example.study_monster_back.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/boards")
@RestController
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/{boardId}/feedback")
    public ResponseEntity<StudyFeedbackResponse> getStudyFeedback(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getStudyFeedback(boardId));
    }
}
