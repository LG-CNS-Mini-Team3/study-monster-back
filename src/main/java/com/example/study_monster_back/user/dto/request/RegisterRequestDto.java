package com.example.study_monster_back.user.dto.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterRequestDto {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String originPwd;
    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String CheckPwd;
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;
    
    private String nickname;
    private String phone_number;
}
