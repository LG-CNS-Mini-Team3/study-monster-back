package com.example.study_monster_back.group.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.study_monster_back.tag.dto.response.TagResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudyGroupResponseDTO {

    private Long id;
    private String name;
    private List<TagResponseDto> tagList;
    private LocalDateTime created_at;
    private String description;
    private Integer limit_members;
    private String deadline;
    private String status;
    private int current;
    private String nickname;
}
