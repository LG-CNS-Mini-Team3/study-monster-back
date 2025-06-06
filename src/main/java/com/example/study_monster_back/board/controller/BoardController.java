package com.example.study_monster_back.board.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.study_monster_back.board.dto.response.BoardResponse;
import com.example.study_monster_back.board.service.BoardSearchService;
import com.example.study_monster_back.board.service.BoardService;
import com.example.study_monster_back.board.dto.request.CreateBoardRequestDto;
import com.example.study_monster_back.board.dto.request.UpdateBoardRequestDto;
import com.example.study_monster_back.board.dto.response.CreateBoardResponseDto;
import com.example.study_monster_back.board.dto.response.GetBoardResponseDto;
import com.example.study_monster_back.board.dto.response.UpdateBoardResponseDto;
import com.example.study_monster_back.board.dto.response.StudyFeedbackResponse;
import com.example.study_monster_back.board.service.BoardService;
import com.example.study_monster_back.tag.dto.response.TagResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Board", description = "게시글 관련 API")
@RequestMapping("/boards")
@RestController
public class BoardController {
    private final BoardService boardService;
    private final BoardSearchService boardSearchService;

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
    public ResponseEntity<GetBoardResponseDto> getBoard(
        @PathVariable(value = "boardId") Long boardId
    ) {
        return ResponseEntity.ok(boardService.getBoard(boardId));
    }

    @PostMapping
    @Operation(summary = "게시글 작성",
        description = "게시글을 작성하고, 해당 게시글의 해시태그도 소문자로 변환하여 저장합니다.")
    public ResponseEntity<CreateBoardResponseDto> createBoard(@Valid @RequestBody CreateBoardRequestDto boardRequestDto,
    @AuthenticationPrincipal UserDetails userdetails) {
        CreateBoardResponseDto boardResponseDto = boardService.createBoard(boardRequestDto, userdetails.getUsername());
        return ResponseEntity.ok(boardResponseDto);
    }

    @PutMapping("/{boardId}")
    @Operation(summary = "게시글 수정", description = "게시글과 게시글 태그를 수정합니다.")
    public ResponseEntity<UpdateBoardResponseDto> updateBoard(@PathVariable Long boardId, @Valid @RequestBody UpdateBoardRequestDto boardRequestDto,
                                                              @AuthenticationPrincipal UserDetails userdetails) {
        UpdateBoardResponseDto updateBoardResponseDto = boardService.updateBoard(boardId,boardRequestDto, userdetails.getUsername());
        return ResponseEntity.ok(updateBoardResponseDto);
    }

    @DeleteMapping("/{boardId}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제하면서 게시글과 연관된 댓글, 좋아요, 피드백, 게시글 태그를 함께 삭제합니다.")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserDetails userdetails) {

        boardService.deleteBoard(boardId, userdetails.getUsername());
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
}
