package com.example.study_monster_back.feedback.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import com.example.study_monster_back.board.entity.Board;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Feedback{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    @CreatedDate
    private LocalDateTime created_at;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
    
}
