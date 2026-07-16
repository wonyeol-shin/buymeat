package com.example.ecommercesystemproject.dashboard.dto;

import java.util.List;

// 통계,위젯 파트와 하나의 /api/dashboard 응답으로 합칠수도 있으니 참고
public record DashboardChartsResponse(
        List<RatingDistribution> reviewRatingDistribution,
        List<CustomerStatusDistribution> customerStatusDistribution,
        List<CategoryDistribution> productCategoryDistribution,
        List<RecentOrderResponse> recentOrders
) {
}