package com.example.study_monster_back.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.study_monster_back.like.entity.Like;
import com.example.study_monster_back.board.entity.Board;

public interface LikeRepository extends JpaRepository<Like, Long> {
    long countByBoardIdAndIsDislike(Long boardId, Long isDislike);
    void deleteAllByBoard(Board board);
    Long countByBoard(Board board);
}
