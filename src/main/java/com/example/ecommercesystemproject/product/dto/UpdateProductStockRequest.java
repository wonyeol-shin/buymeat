package com.example.ecommercesystemproject.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateProductStockRequest {
    @NotBlank
    private Integer stock;
}
