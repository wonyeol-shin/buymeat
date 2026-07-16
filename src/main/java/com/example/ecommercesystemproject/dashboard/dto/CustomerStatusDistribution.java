package com.example.ecommercesystemproject.dashboard.dto;

import com.example.ecommercesystemproject.customer.enums.CustomerStatus;

// 상태별 고객 수
public record CustomerStatusDistribution(CustomerStatus status, Long count) {
}
