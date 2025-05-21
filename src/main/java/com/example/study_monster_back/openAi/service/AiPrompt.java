package com.example.study_monster_back.openAi.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AiPrompt {
    STUDY_FEEDBACK_SYSTEM("""
        You are an expert in writing technical columns.
        Read the article and let me know what the writer needs to improve on in the article
         and what parts of it would be good to study further.
        Answer in Korean.
        """),
    STUDY_FEEDBACK_USER("""
        <title>
            %s
        </title>
        <article>
            %s
        </article>
        """),
    STUDY_FEEDBACK_PROPERTY_DESCRIPTION_FEEDBACK("Parts that the author should correct in the article"),
    STUDY_FEEDBACK_PROPERTY_DESCRIPTION_STRATEGY("Parts of the text that the author recommends studying in more detail or for additional study"),
    ;
    private final String prompt;
}
