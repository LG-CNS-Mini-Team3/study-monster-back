package com.example.study_monster_back.tag.service;

import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.tag.entity.BoardTag;
import com.example.study_monster_back.tag.entity.Tag;
import com.example.study_monster_back.tag.repository.BoardTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardTagServiceImpl implements BoardTagService {

    private final BoardTagRepository boardTagRepository;

    public BoardTag createBoardTag(Board board, Tag tag) {
        BoardTag boardTag = BoardTag.builder()
                .board(board)
                .tag(tag)
                .build();
        return boardTagRepository.save(boardTag);
    }


}
