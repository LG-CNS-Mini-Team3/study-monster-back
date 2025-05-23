package com.example.study_monster_back.tag.service;

import com.example.study_monster_back.group.entity.StudyGroup;
import com.example.study_monster_back.tag.dto.response.PopularTagResponseDto;
import com.example.study_monster_back.tag.entity.StudyGroupTag;
import com.example.study_monster_back.tag.entity.Tag;
import com.example.study_monster_back.tag.repository.StudyGroupTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<PopularTagResponseDto> getPopularTags() {
        return studyGroupTagRepository.findTop10PopularTags().stream()
                .map(result -> PopularTagResponseDto.from((Tag) result[0], (Long) result[1]))
                .toList();
    }
}
