package com.example.study_monster_back.group.entity;


import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.example.study_monster_back.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class StudyGroup{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @CreatedDate
    private LocalDateTime created_at;
    private String description;
    private Integer limit_members;
    private LocalDateTime deadline;
    private String status;

    @ManyToOne
    private User creator;
}
