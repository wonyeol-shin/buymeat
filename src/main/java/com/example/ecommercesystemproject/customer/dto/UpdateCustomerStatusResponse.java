package com.example.ecommercesystemproject.customer.dto;

import com.example.ecommercesystemproject.customer.enums.CustomerStatus;
import lombok.Getter;

@Getter
public class UpdateCustomerStatusResponse {

    private final  Long id;
    private final CustomerStatus status;

    public UpdateCustomerStatusResponse(Long id, CustomerStatus status) {
        this.id = id;
        this.status = status;
    }
}
