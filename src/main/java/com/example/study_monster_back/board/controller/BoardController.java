package com.example.study_monster_back.board.controller;

import com.example.study_monster_back.board.dto.request.CreateBoardRequestDto;
import com.example.study_monster_back.board.dto.request.UpdateBoardRequestDto;
import com.example.study_monster_back.board.dto.response.CreateBoardResponseDto;
import com.example.study_monster_back.board.dto.response.GetBoardResponseDto;
import com.example.study_monster_back.board.dto.response.UpdateBoardResponseDto;
import com.example.study_monster_back.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Board", description = "게시글 관련 API")
@RequestMapping("/boards")
@RestController
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/{boardId}")
    public ResponseEntity<GetBoardResponseDto> getBoard(
        @PathVariable(value = "boardId") Long boardId
    ) {
        return ResponseEntity.ok(boardService.getBoard(boardId));
    }

    @PostMapping
    @Operation(summary = "게시글 작성",
            description = "게시글을 작성하고, 해당 게시글의 해시태그도 소문자로 변환하여 저장합니다.")
    public ResponseEntity<CreateBoardResponseDto> createBoard(@Valid @RequestBody CreateBoardRequestDto boardRequestDto) {
        // TODO: 추후에 @AuthenticationPrincipal로 유저 정보 가져올 예정
        CreateBoardResponseDto boardResponseDto = boardService.createBoard(boardRequestDto);
        return ResponseEntity.ok(boardResponseDto);
    }

    @PutMapping("/{boardId}")
    @Operation(summary = "게시글 수정", description = "게시글과 게시글 태그를 수정합니다.")
    public ResponseEntity<UpdateBoardResponseDto> updateBoard(@PathVariable Long boardId, @Valid @RequestBody UpdateBoardRequestDto boardRequestDto) {
        // TODO: 추후에 @AuthenticationPrincipal로 유저 정보 가져와서 자신의 게시글인 경우에만 수정
        UpdateBoardResponseDto updateBoardResponseDto = boardService.updateBoard(boardId,boardRequestDto);
        return ResponseEntity.ok(updateBoardResponseDto);
    }

    @DeleteMapping("/{boardId}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제하면서 게시글과 연관된 댓글, 좋아요, 피드백, 게시글 태그를 함께 삭제합니다.")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {
        // TODO: 추후에 @AuthenticationPrincipal로 유저 정보 가져와서 자신의 게시글일 경우에만 삭제
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok("게시글을 정상적으로 삭제했습니다.");
    }
}
