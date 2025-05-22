package com.example.study_monster_back.group.controller;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.study_monster_back.group.dto.StudyGroupRequestDTO;
import com.example.study_monster_back.group.dto.StudyGroupResponseDTO;
import com.example.study_monster_back.group.service.StudyGroupService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin
@RestController
@RequestMapping("/study-groups")
@RequiredArgsConstructor
public class StudyGroupController {

    private final StudyGroupService studyGroupService;

    @GetMapping()
    public ResponseEntity<List<StudyGroupResponseDTO>> getList() {
        List<StudyGroupResponseDTO> studyGroups = studyGroupService.getAllStudyGroups();
        return ResponseEntity.ok(studyGroups);
    }
    @PostMapping()
    public ResponseEntity<?> createStudyGroup(
        @RequestBody StudyGroupRequestDTO dto,
        @RequestParam Long userId) { //아이디는 테스트용

        studyGroupService.create(dto, userId);
        return ResponseEntity.ok(Map.of("message", "스터디 생성 완료"));
    }

    @GetMapping("/{studyId}")
    public ResponseEntity<StudyGroupResponseDTO> getStudyGroupById(@PathVariable Long studyId) {
        return ResponseEntity.ok(studyGroupService.getById(studyId));

    }

    @GetMapping("/search/tags")
    @Operation(summary = "태그로 스터디그룹 조회", description = "해당 태그들을 하나라도 가진 스터디그룹을 조회합니다.")
    public ResponseEntity<Page<StudyGroupResponseDTO>> searchByTags(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) List<String> tags) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        return ResponseEntity.ok(studyGroupService.getStudyGroupsByTags(tags, pageable));
    }
}
