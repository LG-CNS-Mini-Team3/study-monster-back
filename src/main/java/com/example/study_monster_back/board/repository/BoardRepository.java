package com.example.study_monster_back.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.study_monster_back.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
        @Query("SELECT b FROM Board b JOIN b.user u " +
                        "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                        "OR LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                        "OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        Page<Board> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
