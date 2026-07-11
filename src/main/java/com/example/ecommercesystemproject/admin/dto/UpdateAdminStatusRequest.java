package com.example.ecommercesystemproject.admin.dto;

import com.example.ecommercesystemproject.admin.entity.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateAdminStatusRequest {
    @NotBlank(message = "상태값은 필수 입니다.")
    private Status status;
}
