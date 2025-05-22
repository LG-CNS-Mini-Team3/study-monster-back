package com.example.study_monster_back.tag.service;

import com.example.study_monster_back.group.entity.StudyGroup;
import com.example.study_monster_back.tag.entity.StudyGroupTag;
import com.example.study_monster_back.tag.entity.Tag;
import com.example.study_monster_back.tag.repository.StudyGroupTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StudyGroupTagServiceImpl implements StudyGroupTagService {

    private final StudyGroupTagRepository studyGroupTagRepository;

    @Override
    public StudyGroupTag createStudyGroupTag(StudyGroup studyGroup, Tag tag) {
        StudyGroupTag studyGroupTag = StudyGroupTag.builder()
                .studyGroup(studyGroup)
                .tag(tag)
                .build();
        return studyGroupTagRepository.save(studyGroupTag);
    }
}
