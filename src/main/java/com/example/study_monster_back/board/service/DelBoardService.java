package com.example.study_monster_back.board.service;

import java.nio.file.AccessDeniedException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.board.repository.BoardRepository;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DelBoardService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public DelBoardService(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void deleteBoard(Long boardId, Long requestUserId) throws AccessDeniedException {
        User user = userRepository.findById(requestUserId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("삭제할 게시글이 존재하지 않습니다."));

        if (!board.getUser().getId().equals(requestUserId) && !"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        boardRepository.delete(board);
    }
}
