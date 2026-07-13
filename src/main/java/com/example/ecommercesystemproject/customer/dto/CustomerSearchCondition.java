package com.example.ecommercesystemproject.customer.dto;

import com.example.ecommercesystemproject.customer.enums.CustomerStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerSearchCondition {
    private String keyword;
    private CustomerStatus status;

    public String getKeyword() {
        return keyword == null || keyword.isBlank() ? null : keyword.trim();
    }
}
