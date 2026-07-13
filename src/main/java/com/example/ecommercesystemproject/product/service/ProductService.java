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

    // 1. 상품 등록
    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request, Long adminId) {
        checkLogin(adminId);

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ServiceException("존재하지 않는 관리자입니다.", HttpStatus.NOT_FOUND));

        // Product 엔티티 생성 시 생성자 파라미터 규격에 맞춰 넘겨줍니다.
        // 만약 Product 엔티티 생성자에 request.getStatus()도 필요하다면 마지막 인자로 추가하세요.
        Product product = new Product(
                request.getProduct_name(),
                request.getCategory(),
                request.getPrice(),
                request.getStock(),
                admin
        );

        Product saveProduct = productRepository.save(product);
        return CreateProductResponse.from(saveProduct);
    }

    // 2. 상품 리스트 조회 - 페이징 + 정렬/필터/키워드
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

        // 응답 DTO 규격(GetProductResponse)에 맞게 매핑
        Page<GetProductResponse> mapped = result.map(GetProductResponse::from);

        return PageResponse.of(mapped);
    }

    // 3. 상품 단 건 상세 조회
    @Transactional(readOnly = true)
    public GetProductResponse getOneProduct(Long id, Long adminId) {
        checkLogin(adminId);

        // N+1 문제를 방지하기 위해 fetch join 쿼리인 findWithAdminById 사용 권장
        // 만약 해당 메서드가 없으면 productRepository.findById(id)로 대체 가능합니다.
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ServiceException("유효하지 않은 상품입니다.", HttpStatus.NOT_FOUND));

        return GetProductResponse.from(product);
    }

    // 4. 상품 정보 수정
    @Transactional
    public UpdateProductResponse updateProduct(Long id, UpdateProductRequest request, Long adminId) {
        checkLogin(adminId);
        Product product = checkKey(id);

        product.editProduct(request.getProduct_name(), request.getCategory(), request.getPrice());
        return new UpdateProductResponse(product.getId());
    }

    // 5. 재고 수정
    @Transactional
    public UpdateProductStockResponse updateProductStock(Long id, UpdateProductStockRequest request, Long adminId) {
        checkLogin(adminId);
        Product product = checkKey(id);

        product.editStock(request.getStock());
        return new UpdateProductStockResponse(product.getId(), product.getStock(), product.getStatus());
    }

    // 6. 상품 상태 수정
    @Transactional
    public UpdateProductStatusResponse updateProductStatus(Long id, UpdateProductStatusRequest request, Long adminId) {
        checkLogin(adminId);
        Product product = checkKey(id);

        product.editStatus(request.getStatus());
        return new UpdateProductStatusResponse(product.getId(), product.getStatus());
    }

    // 7. 상품 삭제
    @Transactional
    public void deleteProduct(Long id, Long adminId) {
        checkLogin(adminId);
        Product product = checkKey(id);

        productRepository.delete(product);
    }

    // [공통 검증] 상품 존재 여부 확인 및 엔티티 반환
    private Product checkKey(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new ServiceException("유효하지 않은 상품입니다.", HttpStatus.NOT_FOUND)
        );
    }

    // [공통 검증] 관리자 로그인 여부 확인
    private void checkLogin(Long loginUserId) {
        if (loginUserId == null) {
            throw new ServiceException("로그인이 필요합니다.", HttpStatus.FORBIDDEN);
        }
    }
}