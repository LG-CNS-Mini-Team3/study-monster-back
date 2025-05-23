package com.example.study_monster_back.like.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LikeCount_OUT {
    private Long like_count;
    private Long dislike_count;

    public LikeCount_OUT(Long likeCount, Long dislikeCount){
        this.like_count = likeCount;
        this.dislike_count = dislikeCount;
    }
}
