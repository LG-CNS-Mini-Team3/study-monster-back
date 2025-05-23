package com.example.study_monster_back.comment.service;

import java.util.List;

import com.example.study_monster_back.comment.dto.Comment_INOUT;

public interface CommentService {
    public void createComment(Comment_INOUT dto);
    public List<Comment_INOUT> getComments(Long boardId);
    public void deleteComment(Comment_INOUT dto);
    public void modifyComment(Comment_INOUT dto);
}
