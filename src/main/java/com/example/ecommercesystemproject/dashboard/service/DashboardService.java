package com.example.ecommercesystemproject.dashboard.service;

import com.example.ecommercesystemproject.customer.repository.CustomerRepository;
import com.example.ecommercesystemproject.dashboard.dto.DashboardChartsResponse;
import com.example.ecommercesystemproject.order.repository.OrderRepository;
import com.example.ecommercesystemproject.product.repository.ProductRepository;
import com.example.ecommercesystemproject.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


        }
}
