package com.example.ecommercesystemproject.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateMyPasswordRequest {
    @NotBlank(message = "기존 패스워드를 입력하세요")
    private String oldPassword;

    @NotBlank(message = "변경 할 패스워드를 입력하세요")
    @Min(value = 6, message = "비밀번호는 최소 6글자 이상 입력하세요")
    @Max(value = 255, message = "비밀번호는 최대 255 글자까지 입력가능합니다.")
    private String newPassword;

    @NotBlank(message = "검증 패스워드를 입력하세요")
    @Min(value = 6, message = "비밀번호는 최소 6글자 이상 입력하세요")
    @Max(value = 255, message = "비밀번호는 최대 255 글자까지 입력가능합니다.")
    private String checkPassword;
}
