package com.example.study_monster_back.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.study_monster_back.group.entity.StudyGroup;
import com.example.study_monster_back.group.entity.StudyMember;
import com.example.study_monster_back.user.entity.User;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
    int countMembersByStudyGroup(StudyGroup group);
    boolean existsByUserAndStudyGroup(User user, StudyGroup group);
}
