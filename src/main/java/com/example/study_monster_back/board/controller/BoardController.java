package com.example.study_monster_back.board.controller;

import com.example.study_monster_back.board.dto.request.CreateBoardRequestDto;
import com.example.study_monster_back.board.dto.response.CreateBoardResponseDto;
import com.example.study_monster_back.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Tag(name = "Board", description = "게시글 관련 API")
@RequestMapping("/api/boards")
@RestController
public class BoardController {

    private final BoardService boardService;

    @CrossOrigin
    @PostMapping
    @Operation(summary = "게시글 작성",
            description = "게시글을 작성하고, 해당 게시글의 해시태그도 소문자로 변환하여 저장합니다.")
    public ResponseEntity<CreateBoardResponseDto> createBoard(@Valid @RequestBody CreateBoardRequestDto boardRequestDto) {
        // TODO: 추후에 @AuthenticationPrincipal로 유저 정보 가져올 예정
        //  security에서 cors 설정할 수 있으므로 임시로 @CrossOrigin 사용
        CreateBoardResponseDto boardResponseDto = boardService.createBoard(boardRequestDto);
        return ResponseEntity.ok(boardResponseDto);
    }

}
