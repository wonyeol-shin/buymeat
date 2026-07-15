package com.example.ecommercesystemproject.dashboard.service;

import com.example.ecommercesystemproject.admin.entity.Status;
import com.example.ecommercesystemproject.admin.repository.AdminRepository;
import com.example.ecommercesystemproject.customer.enums.CustomerStatus;
import com.example.ecommercesystemproject.customer.repository.CustomerRepository;
import com.example.ecommercesystemproject.dashboard.dto.DashboardChartsResponse;
import com.example.ecommercesystemproject.dashboard.dto.RatingDistribution;
import com.example.ecommercesystemproject.dashboard.dto.widget.DashboardWidgetDto;
import com.example.ecommercesystemproject.order.entity.OrderStatus;
import com.example.ecommercesystemproject.dashboard.dto.DashboardSummaryResponse;
import com.example.ecommercesystemproject.order.repository.OrderRepository;
import com.example.ecommercesystemproject.product.entity.ProductStatus;
import com.example.ecommercesystemproject.product.repository.ProductRepository;
import com.example.ecommercesystemproject.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor

public class DashboardService {

        private final ReviewRepository reviewRepository;
        private final CustomerRepository customerRepository;
        private final ProductRepository productRepository;
        private final OrderRepository orderRepository;
        private final AdminRepository adminRepository;

        private static final int RECENT_ORDER_LIMIT = 10;

        @Transactional(readOnly = true)
        public DashboardChartsResponse getDashboardCharts() {

            // 리뷰 평점 분포
            List<RatingDistribution> reviewRatingDistribution = reviewRepository.countGroupByGrade();
        }

        // Summary 통계
        public DashboardSummaryResponse getDashboardSummary() {
                // 고객 통계
                long allCustomers = customerRepository.count();
                long activeCustomers = customerRepository.countByStatus(CustomerStatus.ACTIVE);

                // 관리자 통계
                long allAdmins = adminRepository.count();
                long activeAdmins = adminRepository.countByStatus(Status.ACTIVE);

                // 상품 통계
                long allProducts = productRepository.count();
                long lowStockProducts = productRepository.countByStockLessThanEqual(5);

                // 주문 통계
                long allOrders = orderRepository.count();
                long todayOrders = orderRepository.countByCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                        LocalDate.now().atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay());

                // 리뷰 통계
                long allReviews = reviewRepository.count();
                Double averageGrade = reviewRepository.findAverageGrade();

                return new DashboardSummaryResponse(
                        allCustomers, activeCustomers,
                        allAdmins, activeAdmins,
                        allProducts, lowStockProducts,
                        allOrders, todayOrders,
                        allReviews, averageGrade
                  );
        }

        @Transactional(readOnly = true)
        public DashboardWidgetDto getDashboardWidget(){

                // 오늘 총 매출
                Long todayTotalSales = orderRepository.sumTotalPriceToday(LocalDate.now().atStartOfDay());

                //준비중 주문 수
                Long preparingCount = orderRepository.countByStatus(OrderStatus.PREPARING);

                // 배송중 주문 수
                Long shippingCount = orderRepository.countByStatus(OrderStatus.SHIPPING);

                // 배송완료 주문 수
                Long deliveredCount = orderRepository.countByStatus(OrderStatus.DELIVERED);

                // 재고 부족 상품 수: 재고 5개 이하
                Long shortOfCount = productRepository.countByStockLessThanEqual(5);

                // 재고 없음(품절) 상품 수
                Long soldOutCount = productRepository.countByStatus(ProductStatus.SOLD_OUT);

                return new DashboardWidgetDto(
                        todayTotalSales,
                        preparingCount,
                        shippingCount,
                        deliveredCount,
                        shortOfCount,
                        soldOutCount
 
                );
        }
}
