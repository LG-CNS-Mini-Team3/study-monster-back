package com.example.study_monster_back.user.dto.response;

import com.example.study_monster_back.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSummaryResponseDto {
    private Long userId;
    private String nickname;

    public static UserSummaryResponseDto from(User user) {
        UserSummaryResponseDto userSummaryResponseDto = UserSummaryResponseDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .build();
        return userSummaryResponseDto;
    }
}
