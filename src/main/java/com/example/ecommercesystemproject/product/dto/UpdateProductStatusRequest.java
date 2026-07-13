package com.example.ecommercesystemproject.product.dto;

import com.example.ecommercesystemproject.product.entity.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateProductStatusRequest {
    @NotNull(message = "상품 상태 입력 누락")
    private ProductStatus status;
}
