package com.example.study_monster_back.tag.service;

import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.tag.entity.BoardTag;
import com.example.study_monster_back.tag.entity.Tag;


public interface BoardTagService {

    BoardTag createBoardTag(Board board, Tag tag);

}