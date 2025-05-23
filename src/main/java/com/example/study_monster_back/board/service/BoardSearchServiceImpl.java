package com.example.study_monster_back.board.service;

import com.example.study_monster_back.board.dto.response.BoardWithTagsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.study_monster_back.board.dto.response.BoardResponse;
import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.board.repository.BoardRepository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardSearchServiceImpl implements BoardSearchService {

    private final BoardRepository boardRepository;

    @Override
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

    @Override
    public Page<BoardWithTagsResponseDto> getBoardsByTags(List<String> tags, Pageable pageable) {

        Page<Board> boards;
        if (tags == null || tags.isEmpty()) {
            boards = boardRepository.findAllWithTags(pageable);
        }
        else {
            tags = tags.stream().filter(tag -> !tag.trim().isEmpty())
                    .map(tag -> tag.trim().toLowerCase()).collect(Collectors.toList());
            boards = boardRepository.findByAnyTags(tags, pageable);
        }
        return boards.map(BoardWithTagsResponseDto::from);
    }


}