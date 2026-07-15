package com.example.ecommercesystemproject.review.repository;

import com.example.ecommercesystemproject.dashboard.dto.RatingDistribution;
import com.example.ecommercesystemproject.review.entity.Review;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(
            value = """
                SELECT r FROM Review r
                JOIN FETCH r.customer c
                JOIN FETCH r.order o
                WHERE (
                    :keyword IS NULL OR
                    :keyword = '' OR
                    r.content LIKE %:keyword% OR
                    c.name LIKE %:keyword%
                )
                """,
            countQuery = """
                SELECT COUNT(r) FROM Review r
                JOIN r.customer c
                JOIN r.order o
                WHERE (
                    :keyword IS NULL OR
                    :keyword = '' OR
                    r.content LIKE %:keyword% OR
                    c.name LIKE %:keyword%
                )
            """
    )
    Page<Review> findAllWithCustomerAndOrder(@NonNull Pageable pageable, String keyword);

    @Query(
            value = """
                SELECT r from Review r
                JOIN FETCH r.customer c
                JOIN FETCH r.order o
                WHERE r.id = :reviewId
                """
    )
    Optional<Review> findByIdWithCustomerAndOrder(@NonNull Long reviewId);

    // dashboard(charts) dto - RatingDistribution
    @Query("SELECT new com.example.ecommercesystemproject.dashboard.dto.RatingDistribution(r.grade, COUNT(r)) " +
            "FROM Review r GROUP BY r.grade")
    List<RatingDistribution> countGroupByGrade();

    // 대시보드 Summary 평균 평점 가져오기
    @Query("""
        SELECT COALESCE(AVG(r.grade), 0)
        FROM Review r
    """)
    Double findAverageGrade();

}