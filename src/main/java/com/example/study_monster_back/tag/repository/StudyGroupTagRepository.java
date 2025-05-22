package com.example.study_monster_back.tag.repository;

import com.example.study_monster_back.tag.entity.StudyGroupTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupTagRepository extends JpaRepository<StudyGroupTag, Long> {
}
