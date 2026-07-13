package com.example.ecommercesystemproject.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateProductRequest {
    @NotBlank
    private String product_name;

    @NotBlank
    private String category;

    @NotBlank
    private Long price;

    @NotBlank
    private Integer stock;

    @NotBlank
    private String status;
}
