package com.example.study_monster_back.like.dto;

import com.example.study_monster_back.like.entity.Like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like_IN {
    private Long id;
    private Long user_id;
    private Long board_id;
    private Long is_dislike;
}