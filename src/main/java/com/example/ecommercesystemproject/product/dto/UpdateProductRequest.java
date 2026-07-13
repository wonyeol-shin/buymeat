package com.example.ecommercesystemproject.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateProductRequest {
    @NotBlank
    private String product_name;

    @NotBlank
    private String category;

    @NotBlank
    private Long price;
}
