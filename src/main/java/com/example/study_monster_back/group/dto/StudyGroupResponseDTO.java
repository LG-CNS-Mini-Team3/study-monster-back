package com.example.study_monster_back.group.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudyGroupResponseDTO {

    private Long id;
    private String name;
    List<String> tags;
    private LocalDateTime created_at;
    private String description;
    private Integer limit_members;
    private LocalDateTime deadline;
    private String status;
    private int current;
    private String nickname;
    private String profileImage;

    
    
}
