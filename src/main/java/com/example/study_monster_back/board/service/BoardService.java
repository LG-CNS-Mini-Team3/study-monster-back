package com.example.study_monster_back.board.service;

import com.example.study_monster_back.board.dto.response.StudyFeedbackResponse;

public interface BoardService {
    StudyFeedbackResponse getStudyFeedback(Long boardId);
}
