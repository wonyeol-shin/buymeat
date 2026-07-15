package com.example.ecommercesystemproject.dashboard.dto.widget;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DashboardWidgetDto {
    // 오늘 총 매출
    private final Long todayTotalSales;
    //준비중 주문 수
    private final Long preparingCount;
    // 배송중 주문 수
    private final Long shippingCount;
    // 배송완료 주문 수
    private final Long deliveredCount;
    // 재고 부족 상품 수: 재고 5개 이하
    private final Long shortOfCount;
    // 재고 없음(품절) 상품 수
    private final Long soldOutCount;
}
