package com.example.ecommercesystemproject.customer.dto;

import com.example.ecommercesystemproject.customer.enums.CustomerStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateCustomerStatusRequest {

    @NotNull
    private CustomerStatus status;
}
