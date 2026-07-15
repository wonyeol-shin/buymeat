package com.example.ecommercesystemproject.dashboard.dto;

public record DashboardSummaryResponse(
        Long allCustomers,
        Long activeCustomers,

        Long allAdmins,
        Long activeAdmins,

        Long allProducts,
        Long lowStockProducts,

        Long allOrders,
        Long todayOrders,

        Long allReviews,
        Double averageGrade
){}
