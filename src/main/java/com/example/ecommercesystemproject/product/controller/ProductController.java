package com.example.ecommercesystemproject.product.controller;

import com.example.ecommercesystemproject.common.constant.SessionConst;
import com.example.ecommercesystemproject.common.response.PageResponse;
import com.example.ecommercesystemproject.product.dto.*;
import com.example.ecommercesystemproject.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import com.example.ecommercesystemproject.admin.dto.AdminSession;
import com.example.ecommercesystemproject.product.dto.*;
import com.example.ecommercesystemproject.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/api/products")
    public ResponseEntity<CreateProductResponse> create(
            @RequestBody CreateProductRequest request,
            HttpServletRequest httpRequest
    ) {
        // 인터셉터(LoginCheckInterceptor)에서 이미 로그인 여부는 검증됨 -> 여기선 세션값만 꺼내 씀
        Long adminId = (Long) httpRequest.getSession().getAttribute(SessionConst.LOGIN_ADMIN_ID);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request, adminId)); // 201
    public ResponseEntity<CreateProductResponse> create
            (@Valid @RequestBody CreateProductRequest request,
            @SessionAttribute(name = "loginAdmin", required = false) AdminSession loginAdmin) {

        Long longAdminId = loginAdmin == null ? null : loginAdmin.getId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(request, longAdminId)); // 201
    }

    // 페이징 + 정렬/필터/키워드 쿼리 파라미터 반영
    @GetMapping("/api/products")
    public ResponseEntity<PageResponse<GetProductResponse>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String order
    ) {
        return ResponseEntity.ok(
                productService.getAllProducts(keyword, category, status, page, size, sort, order)
        );
    }

    @GetMapping("/api/products/{productId}")
    public ResponseEntity<GetProductDetailResponse> getOne(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getOneProduct(productId));
    public ResponseEntity<List<GetProductsResponse>> getAll
            (@SessionAttribute(name = "loginAdmin", required = false) AdminSession loginAdmin) {

        Long longAdminId = loginAdmin == null ? null : loginAdmin.getId();
        return ResponseEntity.ok(productService.getAllProducts(longAdminId)); // 200
    }

    @GetMapping("/api/products/{productId}")
    public ResponseEntity<GetProductResponse> getOne
            (@PathVariable Long productId,
             @SessionAttribute(name = "loginAdmin", required = false) AdminSession loginAdmin) {

        Long longAdminId = loginAdmin == null ? null : loginAdmin.getId();
        return ResponseEntity.ok(productService.getOneProduct(productId, longAdminId));
    }

    @PutMapping("/api/products/{productId}")
    public ResponseEntity<UpdateProductResponse> update
            (@PathVariable Long productId, @Valid @RequestBody UpdateProductRequest request,
             @SessionAttribute(name = "loginAdmin", required = false) AdminSession loginAdmin) {

        Long longAdminId = loginAdmin == null ? null : loginAdmin.getId();
        return ResponseEntity.ok(productService.updateProduct(productId, request, longAdminId));
    }

    @PatchMapping("/api/products/{productId}/stock")
    public ResponseEntity<UpdateProductStockResponse> updateStock
            (@PathVariable Long productId, @Valid @RequestBody UpdateProductStockRequest request,
             @SessionAttribute(name = "loginAdmin", required = false) AdminSession loginAdmin) {

        Long longAdminId = loginAdmin == null ? null : loginAdmin.getId();
        return ResponseEntity.ok(productService.updateProductStock(productId, request, longAdminId));
    }

    @PatchMapping("/api/products/{productId}/status")
    public ResponseEntity<UpdateProductStatusResponse> updateStatus
            (@PathVariable Long productId, @Valid @RequestBody UpdateProductStatusRequest request,
             @SessionAttribute(name = "loginAdmin", required = false) AdminSession loginAdmin) {

        Long longAdminId = loginAdmin == null ? null : loginAdmin.getId();
        return ResponseEntity.ok(productService.updateProductStatus(productId, request, longAdminId));
    }

    @DeleteMapping("/api/products/{productId}")
    public ResponseEntity<Void> delete
            (@PathVariable Long productId,
             @SessionAttribute(name = "loginAdmin", required = false) AdminSession loginAdmin) {

        Long longAdminId = loginAdmin == null ? null : loginAdmin.getId();
        productService.deleteProduct(productId, longAdminId);
        return ResponseEntity.noContent().build(); // 204
    }
}