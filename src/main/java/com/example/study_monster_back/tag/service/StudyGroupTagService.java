package com.example.study_monster_back.tag.service;

import com.example.study_monster_back.group.entity.StudyGroup;
import com.example.study_monster_back.tag.dto.response.PopularTagResponseDto;
import com.example.study_monster_back.tag.entity.StudyGroupTag;
import com.example.study_monster_back.tag.entity.Tag;

import java.util.List;

public interface StudyGroupTagService {

    StudyGroupTag createStudyGroupTag(StudyGroup studyGroup, Tag tag);

    List<PopularTagResponseDto> getPopularTags();
}
