package com.example.ecommercesystemproject.dashboard.dto;

// 최근 주문 목록
public record RecentOrderResponse(
        String orderNumber,
        String customerName,
        String productName,
        Long totalPrice,
        String status
) {
}
