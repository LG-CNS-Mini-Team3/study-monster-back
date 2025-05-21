package com.example.study_monster_back.feedback.repository;

import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.feedback.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
  
    void deleteAllByBoard(Board board);
  
    Long countByBoard(Board board);

    Optional<Feedback> findByBoard(Board board);
}