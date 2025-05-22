package com.example.study_monster_back.group.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.study_monster_back.group.entity.StudyGroup;
import com.example.study_monster_back.group.entity.StudyMember;
import com.example.study_monster_back.group.exception.StudyJoinException;
import com.example.study_monster_back.group.repository.StudyGroupRepository;
import com.example.study_monster_back.group.repository.StudyMemberRepository;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyMemberServiceImpl implements StudyMemberService {

    private final StudyGroupRepository studyGroupRepository;
    private final UserRepository userRepository;
    private final StudyMemberRepository studyMemberRepository;

    @Override
    @Transactional
    public void join(Long studyId, Long userId) {
        log.info("스터디 신청 요청 - studyGroupId: {}, userId: {}", studyId, userId); //로그확인

        StudyGroup group = studyGroupRepository.findById(studyId)
            .orElseThrow(() -> {
                log.error("스터디 그룹이 존재하지 않음. ID: {}", studyId);
                return new RuntimeException("스터디 그룹 없음");
            });

        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                log.error("사용자 없음. ID: {}", userId);
                return new RuntimeException("사용자 없음");
            });

        log.info("유저와 그룹 조회 완료. 유저: {}, 그룹: {}", user.getNickname(), group.getName());

        // 중복 체크
        if (studyMemberRepository.existsByUserAndStudyGroup(user, group)) {
            log.warn("이미 신청한 유저입니다.");
            throw new StudyJoinException("이미 신청한 유저입니다.");
        }
        // 마감 지남
        if (group.getDeadline().isBefore(LocalDateTime.now())) {
            log.warn("스터디 마감일 초과 - 마감일: {}", group.getDeadline());
            throw new StudyJoinException("스터디 모집 마감일이 지났습니다.");
        }

        int currentMembers = studyMemberRepository.countMembersByStudyGroup(group);
        //인원초과
        if(currentMembers == group.getLimit_members()) {
            log.warn("스터디 인원 초과 - 현재: {}, 제한: {}", currentMembers, group.getLimit_members());
            throw new StudyJoinException("스터디 정원이 초과되어 신청할 수 없습니다.");
        }

        StudyMember member = new StudyMember();
        member.setUser(user);
        member.setStudyGroup(group);
        studyMemberRepository.save(member);

        log.info("스터디 신청 저장 완료");
    }
}
