package com.example.ecommercesystemproject.common.response;

import org.springframework.data.domain.Page;

import java.util.List;

// 공통 페이징 응답 Customer, Order도 재사용 가능

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber() + 1, // Spring Pageable은 0-base, 우리 API 스펙은 1-base라서 +1
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}