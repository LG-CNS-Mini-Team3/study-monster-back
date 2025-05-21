package com.example.study_monster_back.group.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.study_monster_back.group.dto.StudyGroupResponseDTO;
import com.example.study_monster_back.group.service.StudyGroupService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin
@RestController
@RequestMapping("/study-groups")
@RequiredArgsConstructor
public class StudyGroupController {

    private final StudyGroupService studyGroupService;

    @GetMapping
    public ResponseEntity<List<StudyGroupResponseDTO>> getList() {
        List<StudyGroupResponseDTO> studyGroups = studyGroupService.getAllStudyGroups();
        return ResponseEntity.ok(studyGroups);
    }

    @GetMapping("/{studyId}")
    public ResponseEntity<StudyGroupResponseDTO> getStudyGroupById(@PathVariable Long studyId) {
    return ResponseEntity.ok(studyGroupService.getById(studyId));
}
   
}
