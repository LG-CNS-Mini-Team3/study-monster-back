package com.example.study_monster_back.board.service;

import com.example.study_monster_back.board.dto.response.GetBoardResponseDto;
import com.example.study_monster_back.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    public GetBoardResponseDto getBoard(Long boardId) {
        return GetBoardResponseDto.builder()
            .board(boardRepository.getBoardByIdWithUser(boardId).orElseThrow(
                () -> new RuntimeException("해당 id를 가진 게시글이 없습니다.")
            ).getBoard())
            .build();
    }
}
