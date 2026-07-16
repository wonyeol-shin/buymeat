package com.example.ecommercesystemproject.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateAdminRequest {
    @NotBlank(message = "이름, 이메일, 핸드폰은 필수값 입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z\\s]+$", message = "이름은 특수문자를 제외한 2~30자리여야 합니다.")
    @Size(min = 2, max = 30,  message = "이름은 특수문자를 제외한 2~30자리여야 합니다.")
    private String name;

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이름, 이메일, 핸드폰은 필수값 입니다.")
    private String email;

    @NotBlank(message = "이름, 이메일, 핸드폰은 필수값 입니다.")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "휴대폰 번호 형식에 맞지 않습니다.")
    private String phone;

}
