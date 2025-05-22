package com.example.study_monster_back.group.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.study_monster_back.tag.dto.response.TagResponseDto;
import com.example.study_monster_back.tag.entity.StudyGroupTag;
import com.example.study_monster_back.tag.entity.Tag;
import com.example.study_monster_back.tag.service.StudyGroupTagService;
import com.example.study_monster_back.tag.service.TagService;
import com.example.study_monster_back.tag.util.TagValidator;
import org.springframework.stereotype.Service;

import com.example.study_monster_back.group.dto.StudyGroupRequestDTO;
import com.example.study_monster_back.group.dto.StudyGroupResponseDTO;
import com.example.study_monster_back.group.entity.StudyGroup;
import com.example.study_monster_back.group.repository.StudyGroupRepository;
import com.example.study_monster_back.group.repository.StudyMemberRepository;
import com.example.study_monster_back.group.entity.StudyMember;
import com.example.study_monster_back.user.entity.User;
import com.example.study_monster_back.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyGroupServiceImpl implements StudyGroupService {

    private final StudyGroupRepository studyGroupRepository;
    private final UserRepository userRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final TagService tagService;
    private final StudyGroupTagService studyGroupTagService;
    private final TagValidator tagValidator;

    @Override
    public List<StudyGroupResponseDTO> getAllStudyGroups() {

        Map<Long, Long> memberCountMap = studyGroupRepository.countMembersByStudyGroup()
                .stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]
                ));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return studyGroupRepository.findAllWithTags().stream()
                .map(group -> {
                    int currentMembers = memberCountMap.getOrDefault(group.getId(), 0L).intValue(); //현재 멤버
                    int limitMembers = group.getLimit_members();
                    boolean isDeadlinePassed = LocalDateTime.now().isAfter(group.getDeadline());
                    boolean isFull = currentMembers == limitMembers;

                    String status = (isDeadlinePassed || isFull) ? "모집완료" : "모집중";
                    String formattedDeadline = group.getDeadline().format(formatter);

                    List<TagResponseDto> tagList = group.getStudyGroupTags().stream()
                            .map(sgt -> TagResponseDto.from(sgt.getTag()))
                            .toList();

                    return new StudyGroupResponseDTO(
                            group.getId(),
                            group.getName(),
                            tagList,
                            group.getCreated_at(),
                            group.getDescription(),
                            group.getLimit_members(),
                            formattedDeadline,
                            status,
                            currentMembers,
                            group.getCreator().getNickname()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public StudyGroupResponseDTO getById(Long id) {
        StudyGroup group = studyGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("스터디 없음"));
        int current = studyMemberRepository.countMembersByStudyGroup(group);

        boolean isDeadlinePassed = LocalDateTime.now().isAfter(group.getDeadline());
        boolean isFull = current >= group.getLimit_members();

        String status = (isDeadlinePassed || isFull) ? "모집완료" : "모집중";
        String formattedDeadline = group.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<TagResponseDto> tagList = group.getStudyGroupTags().stream()
                .map(sgt -> TagResponseDto.from(sgt.getTag()))
                .toList();

        return new StudyGroupResponseDTO(
                group.getId(),
                group.getName(),
                tagList,
                group.getCreated_at(),
                group.getDescription(),
                group.getLimit_members(),
                formattedDeadline,
                status,
                current,
                group.getCreator().getNickname()
        );
    }

    @Override
    @Transactional
    public void create(StudyGroupRequestDTO dto, Long userId) {

        //사용자 조회

        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));


        if (dto.getLimit_members() < 2) {
            throw new IllegalArgumentException("모집 인원은 2명 이상이어야 합니다.");
        }

        if (dto.getDeadline().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("모집 마감일은 현재 시각 이후여야 합니다.");
        }

        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("스터디 제목을 입력해 주세요.");
        }

        if (dto.getDescription() == null || dto.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("스터디 설명을 입력해 주세요.");
        }

        // StudyGroup 생성
        StudyGroup group = new StudyGroup();
        group.setName(dto.getName());
        group.setDescription(dto.getDescription());
        group.setDeadline(dto.getDeadline());
        group.setLimit_members(dto.getLimit_members());
        group.setStatus("모집중");
        group.setCreator(creator);

        studyGroupRepository.save(group);

        // 3 방장도 멤버
        StudyMember member = new StudyMember();
        member.setUser(creator);
        member.setStudyGroup(group);

        studyMemberRepository.save(member);


        for (String tagName : tagValidator.filterValidTags(dto.getTags())) {
            Tag tag = tagService.findOrCreateTag(tagName);
            StudyGroupTag studyGroupTag = studyGroupTagService.createStudyGroupTag(group, tag);
            group.addStudyGroupTag(studyGroupTag);
        }

    }
}
