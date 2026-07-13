package com.example.ecommercesystemproject.product.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class CreateProductRequest {
    @NotBlank(message = "상품명 입력 누락")
    private String product_name;

    @NotBlank(message = "목록 입력 누락")
    private String category;

    @NotNull(message = "가격 입력 누락")
    @PositiveOrZero(message = "유효하지 않은 가격 값")
    private Long price;

    @NotNull(message = "재고 입력 누락")
    @PositiveOrZero(message = "유효하지 않은 재고 값")
    private Integer stock;

    @NotNull(message = "상품 상태 입력 누락")
    private String status;
}
