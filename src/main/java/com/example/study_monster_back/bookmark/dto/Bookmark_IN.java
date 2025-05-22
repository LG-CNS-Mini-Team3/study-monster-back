package com.example.study_monster_back.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark_IN {
    private Long user_id;
    private Long board_id;
}
