package com.example.study_monster_back.like.service;

import com.example.study_monster_back.like.dto.LikeCount_OUT;
import com.example.study_monster_back.like.dto.Like_IN;


public interface LikeService {
    public void chu(Like_IN dto);
    public LikeCount_OUT getCount(Long boardId);
}
