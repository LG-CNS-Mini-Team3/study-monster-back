package com.example.study_monster_back.group.service;

import java.util.List;

import com.example.study_monster_back.group.dto.StudyGroupRequestDTO;
import com.example.study_monster_back.group.dto.StudyGroupResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudyGroupService {
    List<StudyGroupResponseDTO> getAllStudyGroups();    
    StudyGroupResponseDTO getById(Long studyId);
    void create(StudyGroupRequestDTO dto, Long userId);
    Page<StudyGroupResponseDTO> getStudyGroupsByTags(List<String> tags, Pageable pageable);
}