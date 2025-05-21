package com.example.study_monster_back.openAi.service;

import com.example.study_monster_back.openAi.dto.response.OpenAiStudyFeedbackResponse;

public interface OpenAiService {
    <T> T callApi(String systemMessage, String userMessage, Class<T> responseType);
    String getPlainAnswer(String systemMessage, String userMessage);
    OpenAiStudyFeedbackResponse getStudyFeedback(String title, String article);
}
