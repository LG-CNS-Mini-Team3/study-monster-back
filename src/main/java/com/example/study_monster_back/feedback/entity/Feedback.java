package com.example.study_monster_back.feedback.entity;

import com.example.study_monster_back.board.entity.Board;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String feedback;
    @Column(columnDefinition = "TEXT")
    private String futureLearningStrategy;
    @CreatedDate
    private LocalDateTime created_at;
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
}
