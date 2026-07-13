package com.example.ecommercesystemproject.review.dto;

import com.example.ecommercesystemproject.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ListReviewResponse {

    private Long id;

    private Integer grade;

    private String content;

    private String customerEmail;

    private String customerName;

    private String orderNumber;

//    TODO: product domain 추가되면 주석 풀기
//    private String productName;

    public static ListReviewResponse from(Review review) {
        return ListReviewResponse.builder()
                .id(review.getId())
                .grade(review.getGrade())
                .content(review.getContent())
                .customerEmail(review.getCustomer().getEmail())
                .customerName(review.getCustomer().getName())
                .orderNumber(review.getOrder().getOrderNumber())
                .build();
    }

}
