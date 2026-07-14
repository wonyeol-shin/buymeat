package com.example.ecommercesystemproject.product.controller;

import com.example.ecommercesystemproject.admin.dto.AdminSession;
import com.example.ecommercesystemproject.common.constant.SessionConst;
import com.example.ecommercesystemproject.product.dto.*;
import com.example.ecommercesystemproject.product.service.ProductService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/api/products")
    public ResponseEntity<CreateProductResponse> create(
            @Valid @RequestBody CreateProductRequest request,
            @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) Long sessionAdminId
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(request, sessionAdminId)); // 201
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<GetProductsResponse>> getAll(
            @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) Long sessionAdminId
    ) {
        return ResponseEntity.ok(productService.getAllProducts(sessionAdminId)); // 200
    }

    @GetMapping("/api/products/{productId}")
    public ResponseEntity<GetProductResponse> getOne(
            @PathVariable Long productId,
            @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) Long sessionAdminId
    ) {

        return ResponseEntity.ok(productService.getOneProduct(productId, sessionAdminId));
    }

    @PutMapping("/api/products/{productId}")
    public ResponseEntity<UpdateProductResponse> update(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request,
            @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) Long sessionAdminId
    ) {

        return ResponseEntity.ok(productService.updateProduct(productId, request, sessionAdminId));
    }

    @PatchMapping("/api/products/{productId}/stock")
    public ResponseEntity<UpdateProductStockResponse> updateStock(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductStockRequest request,
            @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) Long sessionAdminId
    ) {

        return ResponseEntity.ok(productService.updateProductStock(productId, request, sessionAdminId));
    }

    @PatchMapping("/api/products/{productId}/status")
    public ResponseEntity<UpdateProductStatusResponse> updateStatus(
            @PathVariable Long productId, @Valid @RequestBody UpdateProductStatusRequest request,
            @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) Long sessionAdminId
    ) {

        return ResponseEntity.ok(productService.updateProductStatus(productId, request, sessionAdminId));
    }

    @DeleteMapping("/api/products/{productId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long productId,
            @Parameter(hidden = true) @SessionAttribute(name = SessionConst.LOGIN_ADMIN_ID) Long sessionAdminId
    ) {

        productService.deleteProduct(productId, sessionAdminId);
        return ResponseEntity.noContent().build(); // 204
    }
}
