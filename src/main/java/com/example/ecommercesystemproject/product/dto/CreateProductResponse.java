package com.example.ecommercesystemproject.product.dto;

import com.example.ecommercesystemproject.product.entity.ProductStatus;

import java.time.LocalDateTime;

public record CreateProductResponse
        (Long id, String product_name, String category, Long price,
         Integer stock, ProductStatus status, LocalDateTime createdAt) {

    public static CreateProductResponse from(Product p) {
        return new CreateProductResponse(p.getId(), p.getProduct_name(), p.getCategory(),
                p.getPrice(), p.getStock(), p.getStatus(), p.getCreatedAt());
    }
}
