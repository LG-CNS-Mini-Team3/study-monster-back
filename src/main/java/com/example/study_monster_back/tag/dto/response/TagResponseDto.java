package com.example.study_monster_back.tag.dto.response;

import com.example.study_monster_back.tag.entity.Tag;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TagResponseDto {
    private Long id;
    private String name;

    public static TagResponseDto from(Tag tag) {
        TagResponseDto tagResponseDto = TagResponseDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
        return tagResponseDto;
    }
}
