package com.example.ecommercesystemproject.dashboard.controller;

import com.example.ecommercesystemproject.admin.dto.AdminSession;
import com.example.ecommercesystemproject.common.annotation.UserInfo;
import com.example.ecommercesystemproject.dashboard.dto.DashboardChartsResponse;
import com.example.ecommercesystemproject.dashboard.dto.DashboardStatsResponse;
import com.example.ecommercesystemproject.dashboard.dto.DashboardSummaryResponse;
import com.example.ecommercesystemproject.dashboard.dto.widget.DashboardWidgetDto;
import com.example.ecommercesystemproject.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class DashboardController {

    private final DashboardService dashboardService;


    // 이것도 GET /api/dashboard 하나로 통합할 것인지 확인 필요
    @GetMapping("api/dashboard/charts")
    public DashboardStatsResponse getDashboardCharts(
            @UserInfo AdminSession adminSession
    ) {
        return dashboardService.getDashboardStats(adminSession.getId());
    }
}
