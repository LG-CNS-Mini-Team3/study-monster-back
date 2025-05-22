package com.example.study_monster_back.openAi.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OpenAiStudyFeedbackResponse {
    private String feedback;
    private String futureLearningStrategy;
}
