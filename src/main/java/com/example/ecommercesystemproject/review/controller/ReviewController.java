package com.example.ecommercesystemproject.review.controller;

import com.example.ecommercesystemproject.common.response.ApiResponse;
import com.example.ecommercesystemproject.review.dto.ListReviewResponse;
import com.example.ecommercesystemproject.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ListReviewResponse>>> getAll(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable,
            @RequestParam(required = false) String searchKeyword
    ) {
        Page<ListReviewResponse> responses = reviewService.getAllReview(pageable, searchKeyword);
        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "리뷰 목록 조회 성공",
                        responses
                )
        );
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ListReviewResponse>> getOne(
            @PathVariable Long reviewId
    ) {
        ListReviewResponse response = reviewService.getOneReview(reviewId);

        return ResponseEntity.ok(
                ApiResponse.of(
                        HttpStatus.OK.value(),
                        "리뷰 단건 조회 성공",
                        response
                )
        );
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

}
