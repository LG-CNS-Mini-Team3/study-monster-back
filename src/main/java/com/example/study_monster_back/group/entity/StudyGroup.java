package com.example.study_monster_back.group.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.study_monster_back.tag.entity.StudyGroupTag;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.study_monster_back.user.entity.User;
import lombok.Data;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
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

    @OneToMany(mappedBy = "studyGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyGroupTag> studyGroupTags = new ArrayList<>();

    public void addStudyGroupTag(StudyGroupTag studyGroupTag){
        studyGroupTags.add(studyGroupTag);
    }
}
