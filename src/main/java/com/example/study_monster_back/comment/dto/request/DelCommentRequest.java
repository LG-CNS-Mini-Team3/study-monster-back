package com.example.study_monster_back.comment.dto.request;

import lombok.Data;

@Data
public class DelCommentRequest {
    private Long commentId;
    private Long adminId;
}
