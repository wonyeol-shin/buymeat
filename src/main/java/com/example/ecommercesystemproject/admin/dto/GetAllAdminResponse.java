package com.example.ecommercesystemproject.admin.dto;

import com.example.ecommercesystemproject.admin.entity.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class GetAllAdminResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final Status status;
    private final LocalDateTime createdAt;
    private final LocalDateTime approvedAt;
}
