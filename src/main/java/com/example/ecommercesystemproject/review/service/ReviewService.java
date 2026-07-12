package com.example.ecommercesystemproject.review.service;

import com.example.ecommercesystemproject.common.ServiceException;
import com.example.ecommercesystemproject.review.dto.ListReviewResponse;
import com.example.ecommercesystemproject.review.entity.Review;
import com.example.ecommercesystemproject.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public List<ListReviewResponse> getAllReview(Pageable pageable, String keyword) {
        return reviewRepository.findAllWithCustomerAndOrder(pageable, keyword).stream()
                .map(ListReviewResponse::from)
                .toList();
    }

    public ListReviewResponse getOneReview(Long reviewId) {
        Review review = reviewRepository.findByIdWithCustomerAndOrder(reviewId)
                .orElseThrow(() -> new ServiceException("Review not found", HttpStatus.NOT_FOUND));

        return ListReviewResponse.from(review);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ServiceException("Review not found", HttpStatus.NOT_FOUND));

        reviewRepository.delete(review);
    }

}
