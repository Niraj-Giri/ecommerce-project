package com.niraj.ecommerce.controller;

import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.dto.ProductResponse;
import com.niraj.ecommerce.service.ProductService;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(
                productService.getProductsByCategory(
                        categoryId,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long productId) {
        ApiResponse<ProductResponse> product=productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }
    @GetMapping("/image/{productId")
    public ResponseEntity<ApiResponse<String>> getProductImageById(@PathVariable Long productId) {
        ApiResponse<String> imageUrl=productService.getImageById(productId);
        return ResponseEntity.ok(imageUrl);

    }
}
