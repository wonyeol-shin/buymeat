package com.example.ecommercesystemproject.auth.dto;

import com.example.ecommercesystemproject.admin.entity.Admin;
import com.example.ecommercesystemproject.admin.entity.Role;
import com.example.ecommercesystemproject.admin.entity.Status;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignupResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final String phone;
    private final Role role;
    private final Status status;
    private final LocalDateTime createdAt;

    public SignupResponse(Admin admin) {
        this.id = admin.getId();
        this.name = admin.getName();
        this.email = admin.getEmail();
        this.phone = admin.getPhone();
        this.role = admin.getRole();
        this.status = admin.getStatus();
        this.createdAt = admin.getCreatedAt();
    }
}
