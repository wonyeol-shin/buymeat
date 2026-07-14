package com.example.ecommercesystemproject.product.dto;

import com.example.ecommercesystemproject.product.entity.*;

import java.time.LocalDateTime;

// 상품 리스트 조회
public record GetProductsResponse
        (Long id, String adminName, String product_name, String category, Long price,
         Integer stock, ProductStatus status, LocalDateTime createdAt) {

    public static GetProductsResponse from(Product p) {
        return new GetProductsResponse
                (p.getId(), p.getAdmin().getName(), p.getProductName(), p.getCategory(),
                p.getPrice(), p.getStock(), p.getStatus(), p.getCreatedAt());
    }
}
