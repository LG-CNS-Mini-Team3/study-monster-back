package com.example.study_monster_back.group.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.study_monster_back.group.entity.StudyGroup;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

    @Query("SELECT s.studyGroup.id, COUNT(s) FROM StudyMember s GROUP BY s.studyGroup.id")
    List<Object[]> countMembersByStudyGroup();

    List<StudyGroup> findByDeadlineBeforeAndStatus(LocalDateTime now, String string);

    @Query("SELECT sg FROM StudyGroup sg LEFT JOIN FETCH sg.studyGroupTags sgt LEFT JOIN FETCH sgt.tag WHERE sg.id = :id")
    Optional<StudyGroup> findByIdWithTags(@Param("id") Long id);

    @Query("SELECT DISTINCT sg FROM StudyGroup sg LEFT JOIN FETCH sg.studyGroupTags sgt LEFT JOIN FETCH sgt.tag")
    List<StudyGroup> findAllWithTags();

    @Query(value = "SELECT DISTINCT sg FROM StudyGroup sg LEFT JOIN FETCH sg.studyGroupTags sgt LEFT JOIN FETCH sgt.tag",
            countQuery = "SELECT COUNT(DISTINCT sg) FROM StudyGroup sg")
    Page<StudyGroup> findAllWithTags(Pageable pageable);

    
}