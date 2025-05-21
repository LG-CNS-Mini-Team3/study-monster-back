package com.example.study_monster_back.board.service;

import java.nio.file.AccessDeniedException;

import org.springframework.stereotype.Service;

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

    public void isAdmin(Long adminId) throws AccessDeniedException {
        User admin = userRepository
                .findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("관리자 계정을 찾을 수 없습니다."));

        if (!"ADMIN".equalsIgnoreCase(admin.getRole())) {
            throw new AccessDeniedException("관리자 권한이 없습니다.");
        }
    }

    public void deleteBoard(Long boardId, Long userId) throws AccessDeniedException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("삭제할 게시글이 존재하지 않습니다."));

        boolean isAuthor = board.getUser().getId().equals(user.getId());
        boolean isAdmin = "ADMIN".equalsIgnoreCase(user.getRole());

        if (!isAuthor && !isAdmin) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        boardRepository.delete(board);
    }
}
