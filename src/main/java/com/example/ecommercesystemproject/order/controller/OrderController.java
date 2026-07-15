package com.example.ecommercesystemproject.order.controller;

import com.example.ecommercesystemproject.admin.dto.AdminSession;
import com.example.ecommercesystemproject.common.annotation.UserInfo;
import com.example.ecommercesystemproject.common.response.ApiResponse;
import com.example.ecommercesystemproject.order.dto.*;
import com.example.ecommercesystemproject.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> create(
            @Valid @RequestBody CreateOrderRequest request,
            @UserInfo AdminSession adminSession
    ) {

        OrderResponse response = orderService.create(request, adminSession.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.of(
                        HttpStatus.CREATED.value(),
                        "주문 생성 성공",
                        response
                )
        );
    }

    // 주문 전체 조회 + 검색, 페이징, 정렬
    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getAll(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable,
            OrderSearchCondition condition
    ) {
        Page<OrderResponse> responses = orderService.getAllOrder(pageable, condition);

        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "주문 목록 조회 성공",
                        responses
                )
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOne(
            @PathVariable Long orderId
    ) {
        OrderResponse response = orderService.getOneOrder(orderId);

        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "주문 상세 조회 성공",
                        response
                )
        );
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderRequest request,
            @UserInfo AdminSession adminSession
    ) {

        OrderResponse response = orderService.updateOrderStatus(orderId, request, adminSession.getId());

        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "주문 상태 변경 성공",
                        response
                )
        );
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancel(
            @PathVariable Long orderId,
            @Valid @RequestBody CancelOrderRequest request,
            @UserInfo AdminSession adminSession
    ) {

        OrderResponse response = orderService.cancelOrder(orderId, request, adminSession.getId());

        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "주문 취소 성공",
                        response
                )
        );
    }
}
