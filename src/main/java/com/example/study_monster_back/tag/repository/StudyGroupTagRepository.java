package com.example.study_monster_back.tag.repository;

import com.example.study_monster_back.tag.entity.StudyGroupTag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyGroupTagRepository extends JpaRepository<StudyGroupTag, Long> {

    @Query("SELECT sgt.tag, COUNT(sgt) FROM StudyGroupTag sgt GROUP BY sgt.tag ORDER BY COUNT(sgt) DESC")
    List<Object[]> findTop10PopularTags(Pageable pageable);

    default List<Object[]> findTop10PopularTags() {
        return findTop10PopularTags(PageRequest.of(0, 10));
    }

}
