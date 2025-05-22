package com.example.study_monster_back.tag.entity;

import com.example.study_monster_back.group.entity.StudyGroup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class StudyGroupTag{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private StudyGroup studyGroup;
}
