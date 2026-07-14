package com.example.ecommercesystemproject.dashboard.service;

import com.example.ecommercesystemproject.customer.repository.CustomerRepository;
import com.example.ecommercesystemproject.dashboard.dto.*;
import com.example.ecommercesystemproject.order.entity.Order;
import com.example.ecommercesystemproject.order.repository.OrderRepository;
import com.example.ecommercesystemproject.product.repository.ProductRepository;
import com.example.ecommercesystemproject.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class DashboardService {

        private final ReviewRepository reviewRepository;
        private final CustomerRepository customerRepository;
        private final ProductRepository productRepository;
        private final OrderRepository orderRepository;

        private static final int RECENT_ORDER_LIMIT = 10;

        @Transactional(readOnly = true)
        public DashboardChartsResponse getDashboardCharts() {

                // 리뷰 평점 분포
                List<RatingDistribution> reviewRatingDistribution = reviewRepository.countGroupByGrade();

                // 고객 상태 분포
                List<CustomerStatusDistribution> customerStatusDistribution = customerRepository.countGroupByStatus();

                // 상품 카테고리 분포
                List<CategoryDistribution> productCategoryDistribution = productRepository.countGroupByCategory();

                // 최근 주문 10개 내림차순
                Page<Order> recentOrdersPage = orderRepository.findRecentOrders(
                        PageRequest.of(0, RECENT_ORDER_LIMIT, Sort.by(Sort.Direction.DESC, "createdAt"))
                );

                List<RecentOrderResponse> recentOrders = recentOrdersPage.getContent().stream()
                        .map(order -> new RecentOrderResponse(
                                order.getOrderNumber(),
                                order.getCustomer().getName(),
                                order.getProduct().getProductName(),
                                order.getTotalPrice(),
                                order.getStatus().name()
                        ))
                        .toList();

                return new DashboardChartsResponse(
                        reviewRatingDistribution,
                        customerStatusDistribution,
                        productCategoryDistribution,
                        recentOrders
                );

        }
}
