package com.example.ecommercesystemproject.product.dto;

import com.example.ecommercesystemproject.product.entity.*;

import java.time.LocalDateTime;

// 상품 상세 조회
public record GetProductResponse
        (String product_name, String category, Long price, Integer stock,
         ProductStatus status, String adminName, String adminEmail, LocalDateTime createdAt) {


    public static GetProductResponse from(Product p) {
        return new GetProductResponse
                (p.getProduct_name(), p.getCategory(), p.getPrice(), p.getStock(), p.getStatus(),
                        p.getAdmin().getName(), p.getAdmin().getEmail(), p.getCreatedAt());
    }
}
