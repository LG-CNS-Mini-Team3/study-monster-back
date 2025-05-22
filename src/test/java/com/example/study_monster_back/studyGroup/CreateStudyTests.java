package com.example.study_monster_back.studyGroup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;


import com.example.study_monster_back.tag.repository.StudyGroupTagRepository;
import com.example.study_monster_back.tag.repository.TagRepository;
import com.example.study_monster_back.tag.service.StudyGroupTagService;
import com.example.study_monster_back.tag.service.StudyGroupTagServiceImpl;
import com.example.study_monster_back.tag.service.TagService;
import com.example.study_monster_back.tag.service.TagServiceImpl;
import com.example.study_monster_back.tag.util.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.study_monster_back.group.dto.StudyGroupRequestDTO;
import com.example.study_monster_back.group.repository.StudyGroupRepository;
import com.example.study_monster_back.group.repository.StudyMemberRepository;
import com.example.study_monster_back.group.service.StudyGroupServiceImpl;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;

public class CreateStudyTests {

    private StudyGroupRepository studyGroupRepository;
    private UserRepository userRepository;
    private StudyMemberRepository studyMemberRepository;
    private StudyGroupServiceImpl studyGroupService;
    private TagService tagService;
    private StudyGroupTagService studyGroupTagService;
    private TagValidator tagValidator;
    private TagRepository tagRepository;
    private StudyGroupTagRepository studyGroupTagRepository;


    @BeforeEach
    void setUp() {
        studyGroupRepository = mock(StudyGroupRepository.class);
        userRepository = mock(UserRepository.class);
        studyMemberRepository = mock(StudyMemberRepository.class);
        tagRepository = mock(TagRepository.class);
        tagService = new TagServiceImpl(tagRepository);
        studyGroupTagRepository = mock(StudyGroupTagRepository.class);
        studyGroupTagService = new StudyGroupTagServiceImpl(studyGroupTagRepository);
        tagValidator = new TagValidator();
        studyGroupService = new StudyGroupServiceImpl(
                studyGroupRepository, userRepository, studyMemberRepository,
                tagService, studyGroupTagService, tagValidator

        );
    }

    @Test
    void 모집인원은2명이상() {
        // given
        StudyGroupRequestDTO dto = new StudyGroupRequestDTO();
        dto.setName("스터디");
        dto.setDescription("설명");
        dto.setLimit_members(1); // 
        dto.setDeadline(LocalDateTime.now().plusDays(3));

        User mockUser = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // when & then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> studyGroupService.create(dto, 1L));
        assertEquals("모집 인원은 2명 이상이어야 합니다.", ex.getMessage());
    }

    @Test
    void 모집마감일이오늘이전일때() {
        StudyGroupRequestDTO dto = new StudyGroupRequestDTO();
        dto.setName("스터디");
        dto.setDescription("설명");
        dto.setLimit_members(3);
        dto.setDeadline(LocalDateTime.now().minusDays(1)); 

        User mockUser = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> studyGroupService.create(dto, 1L));
        assertEquals("모집 마감일은 현재 시각 이후여야 합니다.", ex.getMessage());
    }

    @Test
    void 스터디이름공백() {
        StudyGroupRequestDTO dto = new StudyGroupRequestDTO();
        dto.setName("  "); // 공백만
        dto.setDescription("설명");
        dto.setLimit_members(3);
        dto.setDeadline(LocalDateTime.now().plusDays(2));

        User mockUser = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> studyGroupService.create(dto, 1L));
        assertEquals("스터디 제목을 입력해 주세요.", ex.getMessage());
    }

    @Test
    void 설명공백일때() {
        StudyGroupRequestDTO dto = new StudyGroupRequestDTO();
        dto.setName("스터디");
        dto.setDescription(" "); // 
        dto.setLimit_members(3);
        dto.setDeadline(LocalDateTime.now().plusDays(2));

        User mockUser = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> studyGroupService.create(dto, 1L));
        assertEquals("스터디 설명을 입력해 주세요.", ex.getMessage());
    }
    
}
