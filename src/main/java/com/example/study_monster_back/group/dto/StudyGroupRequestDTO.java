package com.example.study_monster_back.group.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class StudyGroupRequestDTO {

    private String name;
    private String description;
    private Integer limit_members;
    private LocalDateTime deadline;
    private String nickname;
    private List<String> tags;
    
}
