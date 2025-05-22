package com.example.study_monster_back.board.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.study_monster_back.board.dto.response.BoardResponse;
import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.board.repository.BoardRepository;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Page<BoardResponse> getBoards(String keyword, String type, Pageable pageable) {
        Page<Board> boards;

        if (keyword == null || keyword.trim().isEmpty()) {
            boards = boardRepository.findAll(pageable);
        } else {
            switch (type) {
                case "title":
                    boards = boardRepository.findByTitleContaining(keyword, pageable);
                    break;
                case "content":
                    boards = boardRepository.findByContentContaining(keyword, pageable);
                    break;
                case "nickname":
                    boards = boardRepository.findByWriterContaining(keyword, pageable);
                    break;
                case "all":
                default:
                    boards = boardRepository.searchByKeyword(keyword, pageable);
                    break;
            }
        }

        return boards.map(BoardResponse::from);
    }
}
