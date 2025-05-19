package com.example.study_monster_back.openAi.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AiMessageRole {
    SYSTEM("system"),
    USER("user"),
    ;
    private final String role;
}
