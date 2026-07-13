package com.example.ecommercesystemproject.product.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class UpdateProductStockRequest {
    @NotNull(message = "재고 입력 누락")
    @PositiveOrZero(message = "유효하지 않은 재고 값")
    private Integer stock;
}
