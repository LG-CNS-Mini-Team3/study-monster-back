package com.example.study_monster_back.group.entity;


import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.study_monster_back.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class StudyMember{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime joined_at;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private StudyGroup studyGroup;
}
