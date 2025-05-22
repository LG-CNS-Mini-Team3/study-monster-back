package com.example.study_monster_back.board.dto.response;

import com.example.study_monster_back.board.entity.Board;
import com.example.study_monster_back.tag.dto.response.TagResponseDto;
import com.example.study_monster_back.user.dto.response.UserSummaryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardWithTagsResponseDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime created_at;
    private UserSummaryResponseDto user;
    private List<TagResponseDto> tags;

    public static BoardWithTagsResponseDto from(Board board) {

        return BoardWithTagsResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .created_at(board.getCreated_at())
                .user(UserSummaryResponseDto.from(board.getUser()))
                .tags(board.getBoardTags().stream()
                        .map(boardTag -> TagResponseDto.from(boardTag.getTag()))
                        .toList())
                .build();
    }
}
