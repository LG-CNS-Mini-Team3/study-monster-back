package com.example.study_monster_back.board.dto.response;

import com.example.study_monster_back.feedback.entity.Feedback;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudyFeedbackResponse {
    String feedback;
    String futureLearningStrategy;

    public StudyFeedbackResponse(Feedback feedback) {
        this.feedback = feedback.getFeedback();
        this.futureLearningStrategy = feedback.getFutureLearningStrategy();
    }
}
