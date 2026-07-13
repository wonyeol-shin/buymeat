package com.example.ecommercesystemproject.product.service;

import com.example.ecommercesystemproject.admin.entity.Admin;
import com.example.ecommercesystemproject.admin.repository.AdminRepository;
import com.example.ecommercesystemproject.common.ServiceException;
import com.example.ecommercesystemproject.common.response.PageResponse;
import com.example.ecommercesystemproject.product.dto.*;
import com.example.ecommercesystemproject.product.entity.Product;
import com.example.ecommercesystemproject.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;

    // 정렬 허용 필드 화이트리스트 (요구사항: 가격/재고/등록일만 허용)
    private static final List<String> ALLOWED_SORT_FIELDS = List.of("price", "stock", "createdAt");

    // 상품 등록
    // (admin_id 연관관계가 필수(nullable=false)로 바뀌면서, 등록 시 로그인한 관리자 정보가 필요해짐 -> adminId 파라미터 추가)
    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request, Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ServiceException("존재하지 않는 관리자입니다.", HttpStatus.NOT_FOUND));

        Product product = new Product(request.getProduct_name(), request.getCategory(),
                request.getPrice(), request.getStock(), request.getStatus(), admin);

        Product saveProduct = productRepository.save(product);

        return new CreateProductResponse(saveProduct.getId(), saveProduct.getProduct_name(),
                saveProduct.getCategory(), saveProduct.getPrice(), saveProduct.getStock(),
                saveProduct.getStatus(), saveProduct.getCreatedAt());
    }

    // 상품 리스트 조회 - 페이징 + 정렬/필터/키워드
    @Transactional(readOnly = true)
    public PageResponse<GetProductResponse> getAllProducts(
            String keyword, String category, String status,
            int page, int size, String sort, String order
    ) {
        String sortField = ALLOWED_SORT_FIELDS.contains(sort) ? sort : "createdAt";
        Sort.Direction direction = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;

        // 요구사항 페이지 번호는 1부터 시작하지만, Spring Pageable은 0부터 시작이라 -1 보정
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, Sort.by(direction, sortField));

        Page<Product> result = productRepository.search(keyword, category, status, pageable);

        // 4. 응답에 등록 관리자명 포함
        Page<GetProductResponse> mapped = result.map(p -> new GetProductResponse(
                p.getId(), p.getProduct_name(), p.getCategory(), p.getPrice(),
                p.getStock(), p.getStatus(), p.getCreatedAt(), p.getAdmin().getName()
        ));

        return PageResponse.of(mapped);
    }

    // 상품 단 건 조회 (4. 등록 관리자명 + 이메일까지 포함)
    @Transactional(readOnly = true)
    public GetProductDetailResponse getOneProduct(Long id) {
        Product product = productRepository.findWithAdminById(id)
                .orElseThrow(() -> new ServiceException("유효하지 않은 상품", HttpStatus.NOT_FOUND));

        return new GetProductDetailResponse(
                product.getId(), product.getProduct_name(), product.getCategory(), product.getPrice(),
                product.getStock(), product.getStatus(), product.getCreatedAt(),
                product.getAdmin().getName(), product.getAdmin().getEmail()
        );
    }

    // 상품 업데이트
    @Transactional
    public UpdateProductResponse updateProduct(Long id, UpdateProductRequest request) {
        Product product = check(id);

        product.editProduct(request.getProduct_name(), request.getCategory(), request.getPrice());
        return new UpdateProductResponse(product.getId());
    }

    // 6. 재고 수정 - 실제 상태 자동 전환 로직은 Product.editStock() 안에서 처리
    @Transactional
    public UpdateProductStockResponse updateProductStock(Long id, UpdateProductStockRequest request) {
        Product product = check(id);

        product.editStock(request.getStock());
        return new UpdateProductStockResponse(product.getId(), product.getStock(), product.getStatus());
    }

    // 상품 상태 수정
    @Transactional
    public UpdateProductStatusResponse updateProductStatus(Long id, UpdateProductStatusRequest request) {
        Product product = check(id);

        product.editStatus(request.getStatus());
        return new UpdateProductStatusResponse(product.getId(), product.getStatus());
    }

    // 상품 삭제
    @Transactional
    public void deleteProduct(Long id) {
        Product product = check(id);

        productRepository.delete(product);
    }

    // 손 댈 상품 키 선정
    private Product check(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new ServiceException("유효하지 않은 상품", HttpStatus.NOT_FOUND) // 404
        );
    }
}