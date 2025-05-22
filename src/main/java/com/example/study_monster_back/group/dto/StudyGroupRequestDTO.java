package com.example.study_monster_back.group.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class StudyGroupRequestDTO {

    private String name;
    private String description;
    private Integer limit_members;
    private LocalDateTime deadline;
    private String nickname;
    //todo 태그 추후 추가
    
}
