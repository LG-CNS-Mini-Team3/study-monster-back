package com.example.study_monster_back.feedback.repository;

import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.feedback.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Optional<Feedback> findByBoard(Board board);
}
