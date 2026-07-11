package com.example.ecommercesystemproject.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class CreateCustomerRequest {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank (message = "올바른 이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 입력해주세요.")
    private String email;

    @NotBlank(message = "- 포함한 휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "- 포함한 휴대폰 번호를 입력해주세요.")
    private String phone;
}
