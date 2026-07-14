package com.example.ecommercesystemproject.product.controller;

import com.example.ecommercesystemproject.admin.dto.AdminSession;
import com.example.ecommercesystemproject.common.constant.SessionConst;
import com.example.ecommercesystemproject.product.dto.*;
import com.example.ecommercesystemproject.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/api/products")
    public ResponseEntity<CreateProductResponse> create
            (@Valid @RequestBody CreateProductRequest request,
            @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID, required = false) AdminSession loginAdmin) {

        Long longAdminId = loginAdmin == null ? null : loginAdmin.getId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(request, longAdminId)); // 201
    }

    // 전체 조회 + 페이징, 검색 기능
    @GetMapping("/api/products")
    public ResponseEntity<Page<GetProductsResponse>> getAll
            (@SessionAttribute(name = "loginAdmin", required = false) AdminSession loginAdmin,
             @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
             ProductSearchCondition condition
            ) {

        Long longAdminId = loginAdmin == null ? null : loginAdmin.getId();
        return ResponseEntity.ok(productService.getAllProducts(longAdminId, pageable, condition)); // 200
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
