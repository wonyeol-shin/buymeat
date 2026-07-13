package com.example.ecommercesystemproject.order.service;

import com.example.ecommercesystemproject.admin.entity.Role;
import com.example.ecommercesystemproject.order.dto.CreateOrderRequest;
import com.example.ecommercesystemproject.order.dto.CreateOrderResponse;
import com.example.ecommercesystemproject.order.entity.Order;
import com.example.ecommercesystemproject.order.repository.OrderRepository;
import com.example.ecommercesystemproject.order.util.OrderNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                "aminName",
                "adminEmail",
                Role.OP
        );
    }
}
