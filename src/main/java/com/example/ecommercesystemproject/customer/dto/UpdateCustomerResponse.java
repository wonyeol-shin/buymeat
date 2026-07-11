package com.example.ecommercesystemproject.customer.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateCustomerResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final String phone;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public UpdateCustomerResponse(Long id, String name, String email, String phone, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
