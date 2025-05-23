package com.example.study_monster_back.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponseDto {
    private String email;
    private String nickname;
    private String name;
}