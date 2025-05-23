package com.example.study_monster_back.tag.repository;

import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.tag.entity.BoardTag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardTagRepository extends JpaRepository<BoardTag, Long> {

    Long countByBoard(Board board);

    @Query("SELECT bt.tag, COUNT(bt) FROM BoardTag bt GROUP BY bt.tag ORDER BY COUNT(bt) DESC")
    List<Object[]> findTop10PopularTags(Pageable pageable);

    default List<Object[]> findTop10PopularTags() {
        return findTop10PopularTags(PageRequest.of(0, 10));
    }

}
