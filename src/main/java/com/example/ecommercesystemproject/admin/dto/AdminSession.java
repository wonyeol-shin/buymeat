package com.example.ecommercesystemproject.admin.dto;

import com.example.ecommercesystemproject.admin.entity.Role;
import com.example.ecommercesystemproject.admin.entity.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 임시용 세션 엔타타
@Getter
@RequiredArgsConstructor
public class AdminSession {
    private final Long id;
    private final Role role;
    private final Status status;
}
