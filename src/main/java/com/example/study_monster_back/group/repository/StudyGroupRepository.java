package com.example.study_monster_back.group.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.study_monster_back.group.entity.StudyGroup;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

    @Query("SELECT s.studyGroup.id, COUNT(s) FROM StudyMember s GROUP BY s.studyGroup.id")
    List<Object[]> countMembersByStudyGroup();

    List<StudyGroup> findByDeadlineBeforeAndStatus(LocalDateTime now, String string);

    
    
}