package com.example.ecommercesystemproject.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RejectAdminRequest {
    @NotBlank(message = "거부 사유를 입력해주세요")
    @Size(min = 2, max = 255,message = "거부 사유는 2자에서 255가 까지 입력하세요" )
    private String rejectReason;
}
