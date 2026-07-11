package com.example.ecommercesystemproject.customer.dto;

import com.example.ecommercesystemproject.customer.enums.CustomerStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateCustomerResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final String phone;
    private final CustomerStatus status;
    // TODO: BASE ENTITIY 만들면 생성일 넣기
//    private final LocalDateTime createdAt;

    public CreateCustomerResponse(Long id, String name, String email, String phone, CustomerStatus status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }
}
