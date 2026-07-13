package com.example.ecommercesystemproject.order.service;

import com.example.ecommercesystemproject.admin.entity.Admin;
import com.example.ecommercesystemproject.admin.entity.Role;
import com.example.ecommercesystemproject.customer.entity.Customer;
import com.example.ecommercesystemproject.order.dto.*;
import com.example.ecommercesystemproject.order.entity.Order;
import com.example.ecommercesystemproject.order.repository.OrderRepository;
import com.example.ecommercesystemproject.order.util.OrderNumberGenerator;
import com.example.ecommercesystemproject.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrder(Pageable pageable, OrderSearchCondition condition) {
        Page<Order> orders = orderRepository.findByKeywordAndStatus(condition.getKeyword(), condition.getStatus(), pageable);
        return orders.map(this::toResponse);
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
        Product product = order.getProduct();
        Customer customer = order.getCustomer();
        Admin admin = order.getAdmin();

        Long adminId = admin != null ? admin.getId() : null;
        String adminName = admin != null ? admin.getName() : null;
        String adminEmail = admin != null ? admin.getEmail() : null;
        Role adminRole = admin != null ? admin.getRole() : null;

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                customer.getName(),
                product.getProduct_name(),
                order.getQuantity(),
                order.getCreatedAt().toLocalDate(),
                order.getStatus(),
                order.getTotalPrice(),
                customer.getEmail(),
                adminId,
                adminName,
                adminEmail,
                adminRole,
                order.getCancellationReason()
        );
    }

    private Order getOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new IllegalStateException("없는 주문입니다.")
        );
    }
}
