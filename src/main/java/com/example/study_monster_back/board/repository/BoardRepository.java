package com.example.study_monster_back.board.repository;

import com.example.study_monster_back.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
