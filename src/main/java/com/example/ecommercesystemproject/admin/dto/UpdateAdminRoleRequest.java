package com.example.ecommercesystemproject.admin.dto;

import com.example.ecommercesystemproject.admin.entity.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateAdminRoleRequest {
    //@NotBlank(message = "역할은 필수값입니다.")
    private Role role ;
}
