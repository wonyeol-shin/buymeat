package com.example.ecommercesystemproject.product.dto;

import com.example.ecommercesystemproject.review.dto.ListReviewResponse;

import java.util.List;
import java.util.Map;

public record ProductDetailReview(
        Integer count,
        Double average,
        Map<Integer, Integer> gradeCntStats,
        List<ListReviewResponse> gradeTop3
) {}