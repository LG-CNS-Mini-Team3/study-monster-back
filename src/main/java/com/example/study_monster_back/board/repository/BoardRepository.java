package com.example.study_monster_back.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.study_monster_back.board.entity.Board;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

}
