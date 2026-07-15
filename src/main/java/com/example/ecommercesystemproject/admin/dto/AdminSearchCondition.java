package com.example.ecommercesystemproject.admin.dto;

import com.example.ecommercesystemproject.admin.entity.Role;
import com.example.ecommercesystemproject.admin.entity.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminSearchCondition {
    private String name;
    private String email;
    private Role role;
    private Status status;
}
