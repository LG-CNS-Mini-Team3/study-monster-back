package com.example.study_monster_back.group.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.study_monster_back.group.dto.StudyGroupResponseDTO;
import com.example.study_monster_back.group.repository.StudyGroupRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyGroupServiceImpl implements StudyGroupService {

    private final StudyGroupRepository studyGroupRepository;


    @Override
    public List<StudyGroupResponseDTO> getAllStudyGroups() {

         Map<Long, Long> memberCountMap = studyGroupRepository.countMembersByStudyGroup()
        .stream()
        .collect(Collectors.toMap(
            row -> (Long) row[0],
            row -> (Long) row[1]
        ));
   
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        return studyGroupRepository.findAll().stream()
        .map(group -> {
            int currentMembers = memberCountMap.getOrDefault(group.getId(), 0L).intValue(); //현재 멤버
            int limitMembers = group.getLimit_members();
            boolean isDeadlinePassed = LocalDateTime.now().isAfter(group.getDeadline());
            boolean isFull = currentMembers == limitMembers;

            String status = (isDeadlinePassed || isFull) ? "모집완료" : "모집중";

            String formattedDeadline = group.getDeadline().toLocalDate().format(formatter);
            List<String> tagNames = Arrays.asList("Java", "Spring", "React"); //태그 임시 확인용

            return new StudyGroupResponseDTO(
                group.getId(),
                group.getName(),
                tagNames,
                group.getCreated_at(),        
                group.getDescription(),       
                group.getLimit_members(),
                formattedDeadline,          
                status,
                currentMembers,
                group.getCreator().getNickname(),           // nickname
                "/images/monster.png"); 
        })
         .collect(Collectors.toList());
    }
}
