package com.example.ecommercesystemproject.order.service;

import com.example.ecommercesystemproject.admin.entity.Admin;
import com.example.ecommercesystemproject.admin.entity.Role;
import com.example.ecommercesystemproject.admin.repository.AdminRepository;
import com.example.ecommercesystemproject.common.exception.ForbiddenException;
import com.example.ecommercesystemproject.common.exception.NotFoundException;
import com.example.ecommercesystemproject.customer.entity.Customer;
import com.example.ecommercesystemproject.customer.repository.CustomerRepository;
import com.example.ecommercesystemproject.order.dto.*;
import com.example.ecommercesystemproject.order.entity.Order;
import com.example.ecommercesystemproject.order.repository.OrderRepository;
import com.example.ecommercesystemproject.order.util.OrderNumberGenerator;
import com.example.ecommercesystemproject.product.entity.Product;
import com.example.ecommercesystemproject.product.repository.ProductRepository;
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
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;

    @Transactional
    public OrderResponse create(CreateOrderRequest request, Long adminId) {
        Product product = getProductOrThrow(request);
        Customer customer = getCustomerOrThrow(request);
        Admin admin = getAdminOrThrow(adminId);

        if (admin.getRole() != Role.SUPER && admin.getRole() != Role.CS) {
            throw new ForbiddenException("주문 생성 권한이 없습니다.");
        }

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
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderRequest request, Long adminId) {

        Admin admin = getAdminOrThrow(adminId);

        if (admin.getRole() != Role.SUPER && admin.getRole() != Role.OP) {
            throw new ForbiddenException("주문 상태 변경 권한이 없습니다.");
        }

        Order order = getOrderOrThrow(orderId);

        order.updateStatus(request.getStatus());

        return toResponse(order);
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId, CancelOrderRequest request, Long adminId) {
        Admin admin = getAdminOrThrow(adminId);

        if (admin.getRole() != Role.SUPER && admin.getRole() != Role.CS) {
            throw new ForbiddenException("주문 취소 권한이 없습니다.");
        }

        Order order = getOrderOrThrow(orderId);
        order.cancel(request.getCancellationReason());

        Product product = order.getProduct();
        product.restoreStock(order.getQuantity());

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
                () -> new NotFoundException("없는 주문입니다.")
        );
    }

    private Product getProductOrThrow(CreateOrderRequest request) {
        return productRepository.findById(request.getProductId()).orElseThrow(
                () -> new NotFoundException("없는 상품입니다.")
        );
    }

    private Customer getCustomerOrThrow(CreateOrderRequest request) {
        return customerRepository.findById(request.getCustomerId()).orElseThrow(
                () -> new NotFoundException("없는 고객입니다.")
        );
    }

    private Admin getAdminOrThrow(Long adminId) {
        return adminRepository.findById(adminId).orElseThrow(
                () -> new NotFoundException("없는 관리자입니다.")
        );
    }
}
