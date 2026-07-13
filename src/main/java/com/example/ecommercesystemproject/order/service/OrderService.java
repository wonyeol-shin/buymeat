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
    public CreateOrderResponse create(CreateOrderRequest request) {

        // long totalPrice = (long) product.getPrice() * request.getQuantity();
        Order order = new Order(
                request.getQuantity(),
                OrderNumberGenerator.generate(),
                100 //임시값
                );
        Order savedOrder = orderRepository.save(order);

        // 임시값 수정 해야함
        return new CreateOrderResponse(
                savedOrder.getId(),
                savedOrder.getOrderNumber(),
                "customer",
                "product",
                savedOrder.getQuantity(),
                savedOrder.getCreatedAt().toLocalDate(),
                savedOrder.getStatus(),
                savedOrder.getTotalPrice(),
                "customerEmail",
                1L,
                "adminName",
                "adminEmail",
                Role.OP
        );
    }

    public List<GetOrderResponse> getAllOrder() {
        return orderRepository.findAll()
                .stream()
                .map(order -> new GetOrderResponse(
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
                        Role.OP
                ))
                .toList();
    }

    public GetOrderResponse getOneOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new IllegalStateException("없는 주문입니다.")
        );
        return new GetOrderResponse(
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
                Role.OP
        );
    }

    @Transactional
    public UpdateOrderResponse update(Long orderId, UpdateOrderRequest request) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new IllegalStateException("없는 주문입니다.")
        );

        order.updateStatus(request.getStatus());

        return new UpdateOrderResponse(
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
                Role.OP
        );
    }


}
