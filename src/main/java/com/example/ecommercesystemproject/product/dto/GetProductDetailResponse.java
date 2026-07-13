package com.example.ecommercesystemproject.product.dto;

import java.time.LocalDateTime;

// 상세조회용 응답 DTO 신규 생성

public record GetProductDetailResponse(
        Long id, String product_name, String category, Long price,
        Integer stock, String status, LocalDateTime createdAt,
        String adminName, String adminEmail
) {
}
