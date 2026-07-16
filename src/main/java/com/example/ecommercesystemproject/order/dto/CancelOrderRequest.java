package com.example.ecommercesystemproject.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CancelOrderRequest {
    @NotBlank(message = "취소 사유를 입력해주세요.")
    private String cancellationReason;
}
