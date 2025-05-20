package com.example.study_monster_back.like.repository;

import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    void deleteAllByBoard(Board board);
}