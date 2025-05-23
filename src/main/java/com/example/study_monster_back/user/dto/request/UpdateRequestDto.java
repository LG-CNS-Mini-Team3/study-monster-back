package com.example.study_monster_back.user.dto.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateRequestDto {
    private String name;
    private String nickname;
    private String newPwd;
    @NotEmpty(message = "기존 비밀번호는 필수 입력 값입니다.")
    private String oldPwd;

}
