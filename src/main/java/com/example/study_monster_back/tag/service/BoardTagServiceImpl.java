package com.example.study_monster_back.tag.service;

import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.tag.dto.response.PopularTagResponseDto;
import com.example.study_monster_back.tag.dto.response.TagResponseDto;
import com.example.study_monster_back.tag.entity.BoardTag;
import com.example.study_monster_back.tag.entity.Tag;
import com.example.study_monster_back.tag.repository.BoardTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardTagServiceImpl implements BoardTagService {

    private final BoardTagRepository boardTagRepository;

    @Override
    public BoardTag createBoardTag(Board board, Tag tag) {
        BoardTag boardTag = BoardTag.builder()
                .board(board)
                .tag(tag)
                .build();
        return boardTagRepository.save(boardTag);
    }

    @Override
    public List<PopularTagResponseDto> getPopularTags() {
        return boardTagRepository.findTop10PopularTags().stream()
                .map(result -> PopularTagResponseDto.from((Tag) result[0], (Long) result[1]))
                .toList();
    }


}
