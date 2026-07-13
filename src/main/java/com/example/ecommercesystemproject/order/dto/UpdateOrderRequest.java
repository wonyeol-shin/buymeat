package com.example.ecommercesystemproject.order.dto;

import com.example.ecommercesystemproject.order.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateOrderRequest {
    @NotNull(message = "주문 상태는 필수입니다.")
    private OrderStatus status;
}
