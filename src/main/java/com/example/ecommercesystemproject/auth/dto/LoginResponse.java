package com.example.ecommercesystemproject.auth.dto;

import com.example.ecommercesystemproject.admin.entity.Admin;
import com.example.ecommercesystemproject.admin.entity.Role;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final Long id;
    private final String email;
    private final Role role;

    public LoginResponse(Admin admin) {
        this.id = admin.getId();
        this.email = admin.getEmail();
        this.role = admin.getRole();
    }

}
