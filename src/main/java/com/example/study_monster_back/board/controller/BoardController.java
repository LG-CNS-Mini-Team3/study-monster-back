package com.example.study_monster_back.board.controller;

import com.example.study_monster_back.board.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.example.study_monster_back.board.service.BoardSearchServiceImpl;
import com.example.study_monster_back.board.service.BoardService;
import com.example.study_monster_back.board.dto.request.CreateBoardRequestDto;
import com.example.study_monster_back.board.dto.request.UpdateBoardRequestDto;
import com.example.study_monster_back.tag.dto.response.TagResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.List;

@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Board", description = "게시글 관련 API")
@RequestMapping("/boards")
@RestController
public class BoardController {
    private final BoardService boardService;
    private final BoardSearchServiceImpl boardSearchService;

    @GetMapping
    public Page<BoardResponse> boardList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(defaultValue = "all") String type) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return boardSearchService.getBoards(keyword, type, pageable);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<GetBoardResponseDto> getBoard(@PathVariable(value = "boardId") Long boardId) {
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
    public ResponseEntity<UpdateBoardResponseDto> updateBoard(@PathVariable Long boardId,
                                                              @Valid @RequestBody UpdateBoardRequestDto boardRequestDto) {
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

    @GetMapping("/{boardId}/tags")
    @Operation(summary = "게시글의 태그 조회", description = "해당 게시글의 태그를 조회합니다.")
    public ResponseEntity<List<TagResponseDto>> getBoardTags(@PathVariable Long boardId) {
        List<TagResponseDto> tagResponseDtoList = boardService.getBoardTags(boardId);
        return ResponseEntity.ok(tagResponseDtoList);
    }

    @GetMapping("/{boardId}/feedback")
    public ResponseEntity<StudyFeedbackResponse> getStudyFeedback(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getStudyFeedback(boardId));
    }

    @GetMapping("/search/tags")
    @Operation(summary = "태그로 게시글 조회", description = "해당 태그들을 하나라도 가진 게시글을 조회합니다.")
    public ResponseEntity<Page<BoardWithTagsResponseDto>> searchByTags(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) List<String> tags) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        return ResponseEntity.ok(boardSearchService.getBoardsByTags(tags, pageable));
    }

}
