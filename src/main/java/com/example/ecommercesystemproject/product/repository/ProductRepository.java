package com.example.ecommercesystemproject.product.repository;

import com.example.ecommercesystemproject.dashboard.dto.CategoryDistribution;
import com.example.ecommercesystemproject.product.entity.Product;
import com.example.ecommercesystemproject.product.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 목록 조회: 키워드(상품명)/카테고리/상태 필터 + 페이징
    // JOIN FETCH로 admin을 함께 가져와서 목록에서 등록 관리자명 조회 시 N+1 방지
    @Query(
            value = "SELECT p FROM Product p JOIN FETCH p.admin " +
                    "WHERE (:keyword IS NULL OR p.productName LIKE CONCAT('%', :keyword, '%')) " +
                    "AND (:category IS NULL OR p.category = :category) " +
                    "AND (:status IS NULL OR p.status = :status)",
            countQuery = "SELECT COUNT(p) FROM Product p " +
                    "WHERE (:keyword IS NULL OR p.productName LIKE CONCAT('%', :keyword, '%')) " +
                    "AND (:category IS NULL OR p.category = :category) " +
                    "AND (:status IS NULL OR p.status = :status)"
    )
    Page<Product> search(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("status") ProductStatus status,
            Pageable pageable
    );

    // 상세 조회: admin을 함께 가져와서 등록 관리자명/이메일 조회 시 추가 쿼리 방지
    @Query("SELECT p FROM Product p JOIN FETCH p.admin WHERE p.id = :id")
    Optional<Product> findWithAdminById(@Param("id") Long id);

    // dashboard(charts) dto - CategoryDistribution
    @Query("SELECT new com.example.ecommercesystemproject.dashboard.dto.CategoryDistribution(p.category, COUNT(p)) " +
            "FROM Product p GROUP BY p.category")
    List<CategoryDistribution> countGroupByCategory();

    // 대시보드 Summary 5개 이하 재고 가져오기
    long countByStockLessThanEqual(Integer stock);
}