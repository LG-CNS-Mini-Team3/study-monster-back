package com.example.study_monster_back.group.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.study_monster_back.group.exception.StudyJoinException;
import com.example.study_monster_back.group.service.StudyMemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/study-groups")
@Slf4j
public class StudyMemberController {

    private final StudyMemberService studyMemberService;

    @PostMapping("/{studyId}/join")
    public ResponseEntity<String> joinStudy(
        @PathVariable Long studyId, @RequestParam Long userId) {

        try {
            log.info("컨트롤러 - 스터디 신청 시작. studyId: {}, userId: {}", studyId, userId);
            studyMemberService.join(studyId, userId);
     
            return ResponseEntity.ok("스터디 신청이 완료되었습니다!");
        } catch (StudyJoinException e) {
            log.warn("비즈니스 로직 예외 발생: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("서버 내부 오류 발생", e);
            return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            }
        }
}
