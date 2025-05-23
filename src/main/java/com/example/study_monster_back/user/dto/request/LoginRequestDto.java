package com.example.study_monster_back.user.dto.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    // null, 빈 문자열, 공백으로 이루어진 문자열 모두 허용x
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String Pwd;
}
