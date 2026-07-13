package com.example.ecommercesystemproject.order.service;

import com.example.ecommercesystemproject.admin.entity.Role;
import com.example.ecommercesystemproject.order.dto.*;
import com.example.ecommercesystemproject.order.entity.Order;
import com.example.ecommercesystemproject.order.repository.OrderRepository;
import com.example.ecommercesystemproject.order.util.OrderNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponse create(CreateOrderRequest request) {

        // long totalPrice = (long) product.getPrice() * request.getQuantity();
        Order order = new Order(
                request.getQuantity(),
                OrderNumberGenerator.generate(),
                100 //임시값
                );
        Order savedOrder = orderRepository.save(order);

        return toResponse(order);
    }

    public List<OrderResponse> getAllOrder() {
        return orderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public OrderResponse getOneOrder(Long orderId) {
        Order order = getOrderOrThrow(orderId);
        return toResponse(order);
    }



    @Transactional
    public OrderResponse updateOrder(Long orderId, UpdateOrderRequest request) {
        Order order = getOrderOrThrow(orderId);

        order.updateStatus(request.getStatus());

        return toResponse(order);
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId, DeleteOrderRequest request) {
        Order order = getOrderOrThrow(orderId);
        order.cancel(request.getCancellationReason());
        return toResponse(order);
    }


    // 임시값 수정 해야함
    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                "customerName",
                "productName",
                order.getQuantity(),
                order.getCreatedAt().toLocalDate(),
                order.getStatus(),
                order.getTotalPrice(),
                "customerEmail",
                1L,
                "adminName",
                "adminEmail",
                Role.OP,
                order.getCancellationReason()
        );
    }

    private Order getOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new IllegalStateException("없는 주문입니다.")
        );
    }
}
