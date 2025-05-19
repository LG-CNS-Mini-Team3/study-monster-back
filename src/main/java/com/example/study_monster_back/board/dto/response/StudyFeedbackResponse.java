package com.example.study_monster_back.board.dto.response;

import com.example.study_monster_back.openAi.dto.response.OpenAiStudyFeedbackResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudyFeedbackResponse {
    String feedback;
    String futureLearningStrategy;

    public StudyFeedbackResponse(OpenAiStudyFeedbackResponse openAiResponse) {
        this.feedback = openAiResponse.getFeedback();
        this.futureLearningStrategy = openAiResponse.getFutureLearningStrategy();
    }
}
