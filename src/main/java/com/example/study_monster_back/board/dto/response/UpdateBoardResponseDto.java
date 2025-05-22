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
public class UpdateBoardResponseDto {

    private Long id;
    private String title;
    private String content;
    private UserSummaryResponseDto user;
    private List<TagResponseDto> tags;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public static UpdateBoardResponseDto from(Board board) {
        return UpdateBoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .user(UserSummaryResponseDto.from(board.getUser()))
                .tags(board.getBoardTags().stream()
                        .map(boardTag -> TagResponseDto.from(boardTag.getTag()))
                        .toList())
                .created_at(board.getCreated_at())
                .updated_at(board.getUpdated_at())
                .build();
    }
}