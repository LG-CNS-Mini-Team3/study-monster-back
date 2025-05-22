package com.example.study_monster_back.group.scheduler;

import com.example.study_monster_back.group.entity.StudyGroup;
import com.example.study_monster_back.group.repository.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StudyStatusScheduler {

    private final StudyGroupRepository studyGroupRepository;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행
    @Transactional
    public void updateClosedStudyGroups() {
        List<StudyGroup> expired = studyGroupRepository.findByDeadlineBeforeAndStatus(
            LocalDateTime.now(), "모집중"
        );

        for (StudyGroup group : expired) {
            group.setStatus("모집완료");
        }

        studyGroupRepository.saveAll(expired);
    }
}
