package com.example.ecommercesystemproject.admin.dto;

import lombok.Getter;

@Getter
public class UpdateMyPasswordRequest {
    private String oldPassword;
    private String newPassword;
}
