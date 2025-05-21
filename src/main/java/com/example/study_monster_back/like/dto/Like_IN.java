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

    
    public Like_IN(Like like){
        this.id = like.getId();
        this.user_id = like.getUser() != null ? like.getUser().getId() : null;
        this.board_id = like.getBoard() != null ? like.getBoard().getId() : null;
        this.is_dislike = like.getIsDislike();
    }
}