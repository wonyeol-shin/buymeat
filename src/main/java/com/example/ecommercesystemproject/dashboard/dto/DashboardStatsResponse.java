package com.example.ecommercesystemproject.dashboard.dto;

import com.example.ecommercesystemproject.dashboard.dto.widget.DashboardWidgetDto;

public record DashboardStatsResponse(
        DashboardChartsResponse chart,
        DashboardSummaryResponse summary,
        DashboardWidgetDto widget
) {
}