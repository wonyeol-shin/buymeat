package com.example.ecommercesystemproject.dashboard.service;

import com.example.ecommercesystemproject.admin.entity.Admin;
import com.example.ecommercesystemproject.admin.entity.Status;
import com.example.ecommercesystemproject.admin.repository.AdminRepository;
import com.example.ecommercesystemproject.common.ServiceException;
import com.example.ecommercesystemproject.customer.enums.CustomerStatus;
import com.example.ecommercesystemproject.customer.repository.CustomerRepository;
import com.example.ecommercesystemproject.dashboard.dto.*;
import com.example.ecommercesystemproject.dashboard.dto.widget.DashboardWidgetDto;
import com.example.ecommercesystemproject.order.entity.Order;
import com.example.ecommercesystemproject.order.entity.OrderStatus;
import com.example.ecommercesystemproject.order.repository.OrderRepository;
import com.example.ecommercesystemproject.product.entity.ProductStatus;
import com.example.ecommercesystemproject.product.repository.ProductRepository;
import com.example.ecommercesystemproject.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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

        private void validateActiveAdmin(Long adminId) {
                Admin admin = adminRepository.findById(adminId)
                        .orElseThrow(() -> new ServiceException(
                                "관리자 정보를 찾을 수 없습니다.",
                                HttpStatus.NOT_FOUND
                        ));

                if (admin.getStatus() != Status.ACTIVE) {
                        throw new ServiceException(
                                "비활성화된 관리자는 대시보드를 조회할 수 없습니다.",
                                HttpStatus.FORBIDDEN
                        );
                }
        }

        @Transactional(readOnly = true)
        public DashboardStatsResponse getDashboardStats(Long adminId) {
                validateActiveAdmin(adminId);

                DashboardChartsResponse charts = getDashboardCharts();
                DashboardSummaryResponse summary = getDashboardSummary();
                DashboardWidgetDto widget = getDashboardWidget();

                return new DashboardStatsResponse(
                        charts,
                        summary,
                        widget
                );
        }

        private DashboardChartsResponse getDashboardCharts() {

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

        // Summary 통계
        private DashboardSummaryResponse getDashboardSummary() {
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

        private DashboardWidgetDto getDashboardWidget(){

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
