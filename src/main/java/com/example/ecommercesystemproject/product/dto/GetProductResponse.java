package com.example.ecommercesystemproject.product.dto;

import com.example.ecommercesystemproject.product.entity.Product;
import com.example.ecommercesystemproject.product.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// 상품 상세 조회

@Builder
@Getter
@Setter
@AllArgsConstructor
public class GetProductResponse {

    private String productName;

    private String category;

    private Long price;

    private Integer stock;

    private ProductStatus status;

    private String adminName;

    private String adminEmail;

    private ProductDetailReview review;

    private LocalDateTime createdAt;

    public static GetProductResponse from(Product p) {
        return new GetProductResponse(
                p.getProductName(),
                p.getCategory(),
                p.getPrice(),
                p.getStock(),
                p.getStatus(),
                p.getAdmin().getName(),
                p.getAdmin().getEmail(),
                null,
                p.getCreatedAt()
        );
    }

}