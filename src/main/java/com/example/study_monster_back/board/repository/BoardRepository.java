package com.example.study_monster_back.board.repository;

import com.example.study_monster_back.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.boardTags bt LEFT JOIN FETCH bt.tag Where b.id = :id")
    Optional<Board> findByIdWithTags(@Param("id") Long id);

}
