package com.example.study_monster_back.tag.dto.response;

import com.example.study_monster_back.tag.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularTagResponseDto {

    private Long id;
    private String name;
    private Long count;

    public static PopularTagResponseDto from(Tag tag, Long count) {
        return PopularTagResponseDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .count(count)
                .build();
    }
}
