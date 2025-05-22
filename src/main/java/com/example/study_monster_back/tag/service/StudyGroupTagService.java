package com.example.study_monster_back.tag.service;

import com.example.study_monster_back.group.entity.StudyGroup;
import com.example.study_monster_back.tag.entity.StudyGroupTag;
import com.example.study_monster_back.tag.entity.Tag;

public interface StudyGroupTagService {

    StudyGroupTag createStudyGroupTag(StudyGroup studyGroup, Tag tag);
}
