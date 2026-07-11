package com.example.ecommercesystemproject.customer.dto;

import com.example.ecommercesystemproject.customer.enums.CustomerStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetCustomerResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final String phone;
    private final CustomerStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public GetCustomerResponse(Long id, String name, String email, String phone, CustomerStatus status, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
