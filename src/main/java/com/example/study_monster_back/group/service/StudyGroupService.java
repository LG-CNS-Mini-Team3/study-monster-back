package com.example.study_monster_back.group.service;

import java.util.List;

import com.example.study_monster_back.group.dto.StudyGroupResponseDTO;

public interface StudyGroupService {
    List<StudyGroupResponseDTO> getAllStudyGroups();
}