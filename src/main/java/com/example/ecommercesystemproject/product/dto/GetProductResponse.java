package com.example.ecommercesystemproject.product.dto;

import com.example.ecommercesystemproject.product.entity.*;

import java.time.LocalDateTime;

public record GetProductResponse(
        Long id, String product_name, String category, Long price,
        Integer stock, String status, LocalDateTime createdAt,
        // 목록용 응답 DTO
        String adminName // 등록 관리자명 추가(새롭게 추가 된 변수)
) {
}
// 상품 상세 조회
public record GetProductResponse
        (String product_name, String category, Long price, Integer stock,
         ProductStatus status, String adminName, String adminEmail, LocalDateTime createdAt) {

    public static GetProductResponse from(Product p) {
        return new GetProductResponse
                (p.getProduct_name(), p.getCategory(), p.getPrice(), p.getStock(), p.getStatus(),
                        p.getAdmin_id().getName(), p.getAdmin_id().getEmail(), p.getCreatedAt());
    }
}
