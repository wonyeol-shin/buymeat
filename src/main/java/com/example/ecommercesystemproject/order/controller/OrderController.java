package com.example.ecommercesystemproject.order.controller;

import com.example.ecommercesystemproject.common.constant.SessionConst;
import com.example.ecommercesystemproject.order.dto.*;
import com.example.ecommercesystemproject.order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> create(
            @Valid @RequestBody CreateOrderRequest request,
            HttpSession session
    ) {
        Long adminId = (Long) session.getAttribute(SessionConst.LOGIN_ADMIN_ID);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(request,adminId));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAll() {
        return ResponseEntity.ok(orderService.getAllOrder());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOne(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOneOrder(orderId));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> update(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderRequest request
    ) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, request));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancel(
            @PathVariable Long orderId,
            @Valid @RequestBody DeleteOrderRequest request
    ) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId, request));
    }
}
