package com.example.ecommercesystemproject.order.controller;

import com.example.ecommercesystemproject.order.dto.CreateOrderRequest;
import com.example.ecommercesystemproject.order.dto.CreateOrderResponse;
import com.example.ecommercesystemproject.order.dto.GetOrderResponse;
import com.example.ecommercesystemproject.order.service.OrderService;
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
    public ResponseEntity<CreateOrderResponse> create(
            @Valid @RequestBody CreateOrderRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<GetOrderResponse>> getAll() {
        return ResponseEntity.ok(orderService.getAllOrder());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<GetOrderResponse> getOne(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOneOrder(orderId));
    }



}
