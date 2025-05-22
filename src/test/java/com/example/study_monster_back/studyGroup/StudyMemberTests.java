package com.example.study_monster_back.studyGroup;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.study_monster_back.group.entity.StudyGroup;
import com.example.study_monster_back.group.exception.StudyJoinException;
import com.example.study_monster_back.group.repository.StudyGroupRepository;
import com.example.study_monster_back.group.repository.StudyMemberRepository;
import com.example.study_monster_back.group.service.StudyMemberServiceImpl;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class StudyMemberTests {
    @InjectMocks
    private StudyMemberServiceImpl studyMemberService;

    @Mock
    private StudyGroupRepository studyGroupRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudyMemberRepository studyMemberRepository;

    private User mockUser;
    private StudyGroup mockGroup;

    @BeforeEach
    void setup() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setNickname("testuser");

        mockGroup = new StudyGroup();
        mockGroup.setId(1L);
        mockGroup.setName("테스트스터디");
        mockGroup.setDeadline(LocalDateTime.now().plusDays(1));
        mockGroup.setLimit_members(5);
    }

    @Test
    void 신청성공() {
        when(studyGroupRepository.findById(1L)).thenReturn(Optional.of(mockGroup));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(studyMemberRepository.existsByUserAndStudyGroup(mockUser, mockGroup)).thenReturn(false);
        when(studyMemberRepository.countMembersByStudyGroup(mockGroup)).thenReturn(3);

        assertDoesNotThrow(() -> studyMemberService.join(1L, 1L));
    }

    @Test
    void 이미신청() {
        when(studyGroupRepository.findById(1L)).thenReturn(Optional.of(mockGroup));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(studyMemberRepository.existsByUserAndStudyGroup(mockUser, mockGroup)).thenReturn(true);

        StudyJoinException e = assertThrows(StudyJoinException.class, () -> studyMemberService.join(1L, 1L));
        assertEquals("이미 신청한 유저입니다.", e.getMessage());
    }

    @Test
    void 데드라인지남() {
        mockGroup.setDeadline(LocalDateTime.now().minusDays(1)); 

        when(studyGroupRepository.findById(1L)).thenReturn(Optional.of(mockGroup));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(studyMemberRepository.existsByUserAndStudyGroup(mockUser, mockGroup)).thenReturn(false);

        StudyJoinException e = assertThrows(StudyJoinException.class, () -> studyMemberService.join(1L, 1L));
        assertEquals("스터디 모집 마감일이 지났습니다.", e.getMessage());
    }

    @Test
    void 정원초과() {
        when(studyGroupRepository.findById(1L)).thenReturn(Optional.of(mockGroup));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(studyMemberRepository.existsByUserAndStudyGroup(mockUser, mockGroup)).thenReturn(false);
        when(studyMemberRepository.countMembersByStudyGroup(mockGroup)).thenReturn(5); 

        StudyJoinException e = assertThrows(StudyJoinException.class, () -> studyMemberService.join(1L, 1L));
        assertEquals("스터디 정원이 초과되어 신청할 수 없습니다.", e.getMessage());
    }

    
}
