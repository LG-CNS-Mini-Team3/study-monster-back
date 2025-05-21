package com.example.study_monster_back.board.dto.db;

import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.user.entity.User;

public interface BoardDetailInfo {
    public Board getBoard();
    public User getWriter();
}
