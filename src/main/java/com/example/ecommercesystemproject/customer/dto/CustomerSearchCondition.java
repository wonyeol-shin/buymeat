package com.example.ecommercesystemproject.customer.dto;

import com.example.ecommercesystemproject.customer.enums.CustomerStatus;
import lombok.Getter;

@Getter
public class CustomerSearchCondition {
    private String keyword;
    private CustomerStatus status;
}
