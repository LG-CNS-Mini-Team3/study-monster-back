package com.example.study_monster_back.studyGroup;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.example.study_monster_back.group.dto.StudyGroupResponseDTO;
import com.example.study_monster_back.group.entity.StudyGroup;
import com.example.study_monster_back.group.repository.StudyGroupRepository;
import com.example.study_monster_back.group.service.StudyGroupServiceImpl;
import com.example.study_monster_back.user.entity.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudyGroupTests {

    @Mock
    private StudyGroupRepository studyGroupRepository;

    @InjectMocks
    private StudyGroupServiceImpl studyGroupService;

    private User creator;

    @BeforeEach
    void setUp() {
        creator = new User();
        creator.setNickname("테스트");
    }

    private StudyGroup createGroup(Long id, String name, LocalDateTime deadline, int limit) {
        StudyGroup group = new StudyGroup();
        group.setId(id);
        group.setName(name);
        group.setDeadline(deadline);
        group.setCreated_at(LocalDateTime.now());
        group.setDescription("설명");
        group.setLimit_members(limit);
        group.setCreator(creator);
        return group;
    }

    @Test
    void 마감시간후모집완료() {
        StudyGroup group = createGroup(1L, "스터디 A", LocalDateTime.now().minusDays(1), 5);

        Object[] row = new Object[]{1L, 3L}; // id = 1, current = 3
        List<Object[]> memberList = Collections.singletonList(row); 

    when(studyGroupRepository.findAll()).thenReturn(List.of(group));
    when(studyGroupRepository.countMembersByStudyGroup()).thenReturn(memberList);

        List<StudyGroupResponseDTO> result = studyGroupService.getAllStudyGroups();

        assertEquals("모집완료", result.get(0).getStatus());
    }

   @Test
void 정원이full모집완료() {
    // 준비
    User creator = new User();
    creator.setNickname("테스트");

    StudyGroup group = new StudyGroup();
    group.setId(1L);
    group.setName("모집완료 테스트");
    group.setDeadline(LocalDateTime.now().plusDays(1)); // 마감일은 아직 안 지남
    group.setCreated_at(LocalDateTime.now());
    group.setDescription("정원 다 찬 스터디");
    group.setLimit_members(3); // 제한 인원

    group.setCreator(creator);

    Object[] row = new Object[]{1L, 3L};
    List<Object[]> memberList = Collections.singletonList(row);

    // mocking
    when(studyGroupRepository.findAll()).thenReturn(List.of(group));
    when(studyGroupRepository.countMembersByStudyGroup()).thenReturn(memberList);

    // 실행
    List<StudyGroupResponseDTO> result = studyGroupService.getAllStudyGroups();

    // 검증
    assertEquals("모집완료", result.get(0).getStatus());
    assertEquals(3, result.get(0).getCurrent());
}

    @Test
    void 멤버정보없으면_기본값0이다() {
        StudyGroup group = createGroup(3L, "스터디 C", LocalDateTime.now().plusDays(2), 10);

        when(studyGroupRepository.findAll()).thenReturn(List.of(group));
        when(studyGroupRepository.countMembersByStudyGroup()).thenReturn(Collections.emptyList());

        List<StudyGroupResponseDTO> result = studyGroupService.getAllStudyGroups();

        assertEquals(0, result.get(0).getCurrent());
        assertEquals("모집중", result.get(0).getStatus());
    }
}
