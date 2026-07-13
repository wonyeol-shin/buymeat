package com.example.ecommercesystemproject.order.service;

import com.example.ecommercesystemproject.admin.entity.Admin;
import com.example.ecommercesystemproject.admin.entity.Role;
import com.example.ecommercesystemproject.admin.repository.AdminRepository;
import com.example.ecommercesystemproject.customer.entity.Customer;
import com.example.ecommercesystemproject.customer.repository.CustomerRepository;
import com.example.ecommercesystemproject.order.dto.*;
import com.example.ecommercesystemproject.order.entity.Order;
import com.example.ecommercesystemproject.order.repository.OrderRepository;
import com.example.ecommercesystemproject.order.util.OrderNumberGenerator;
import com.example.ecommercesystemproject.product.entity.Product;
import com.example.ecommercesystemproject.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;

    @Transactional
    public OrderResponse create(CreateOrderRequest request, Long adminId) {
        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new IllegalStateException("없는 상품입니다.")
        );
        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(
                () -> new IllegalStateException("없는 고객입니다.")
        );
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalStateException("없는 관리자입니다.")
        );
        long totalPrice = product.getPrice() * request.getQuantity();

        Order order = new Order(
                request.getQuantity(),
                OrderNumberGenerator.generate(),
                admin,
                product,
                customer,
                totalPrice
        );
        Order savedOrder = orderRepository.save(order);
        return toResponse(savedOrder);
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
