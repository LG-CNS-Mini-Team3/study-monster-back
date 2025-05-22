package com.example.study_monster_back.tag.repository;

import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.tag.entity.BoardTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardTagRepository extends JpaRepository<BoardTag, Long> {
    Long countByBoard(Board board);
}
