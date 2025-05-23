package com.example.study_monster_back.tag.service;

import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.tag.dto.response.PopularTagResponseDto;
import com.example.study_monster_back.tag.dto.response.TagResponseDto;
import com.example.study_monster_back.tag.entity.BoardTag;
import com.example.study_monster_back.tag.entity.Tag;

import java.util.List;


public interface BoardTagService {

    BoardTag createBoardTag(Board board, Tag tag);

    List<PopularTagResponseDto> getPopularTags();
}