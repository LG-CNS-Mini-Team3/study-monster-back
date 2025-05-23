package com.example.study_monster_back.group.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<?> createStudyGroup(@RequestBody StudyGroupRequestDTO dto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName(); // 로그인된 사용자의 이메일

   // Long  = studyGroupService.findUserIdByEmail(email); // 서비스에서 userId 가져오기
    studyGroupService.create(dto, email);

    return ResponseEntity.ok(Map.of("message", "스터디 생성 완료"));
}

    @GetMapping("/{studyId}")
    public ResponseEntity<StudyGroupResponseDTO> getStudyGroupById(@PathVariable Long studyId) {
        return ResponseEntity.ok(studyGroupService.getById(studyId));

    }
}
