package com.example.study_monster_back.tag.controller;

import com.example.study_monster_back.tag.dto.response.PopularTagResponseDto;
import com.example.study_monster_back.tag.service.BoardTagService;
import com.example.study_monster_back.tag.service.StudyGroupTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/tags")
@RestController
public class TagController {

    private final BoardTagService boardTagService;
    private final StudyGroupTagService studyGroupTagService;

    @GetMapping("/popular/boards")
    public ResponseEntity<List<PopularTagResponseDto>> getPopularBoardTags() {
        return ResponseEntity.ok(boardTagService.getPopularTags());
    }

    @GetMapping("/popular/study-groups")
    public ResponseEntity<List<PopularTagResponseDto>> getPopularStudyGroupTags() {
        return ResponseEntity.ok(studyGroupTagService.getPopularTags());
    }

}
