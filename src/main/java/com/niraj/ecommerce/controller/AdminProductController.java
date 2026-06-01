package com.niraj.ecommerce.controller;

import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.dto.CategoryAddRequest;
import com.niraj.ecommerce.dto.ProductAddRequest;
import com.niraj.ecommerce.dto.ProductResponse;
import com.niraj.ecommerce.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/product")
public class AdminProductController {

       private final ProductService productService;
       public AdminProductController(ProductService productService) {
              this.productService = productService;
       }

     @GetMapping("/{categoryId}")
     public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProductsByCategoryId(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ApiResponse<Page<ProductResponse>> products =
                productService.findByCategoryId(categoryId, page, size);

        return ResponseEntity.ok(products);
    }
        @PostMapping("/category/add/{id}")
        @PreAuthorize("hasAuthority('ADMIN')")
         public ResponseEntity<ApiResponse> addProduct(@PathVariable  Long id, @RequestBody ProductAddRequest  productAddRequest) {
           ApiResponse<Void> apiResponse=productService.addProduct(productAddRequest,id);
           return ResponseEntity.ok(apiResponse);
        }
       @PutMapping("update/{id}")
       @PreAuthorize("hasAuthority('ADMIN')")
        public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductAddRequest  productAddRequest , @PathVariable Long id){
        ApiResponse<Void> apiResponse=productService.updateCategory(  productAddRequest,id);
         return ResponseEntity.ok(apiResponse);
       }
       @DeleteMapping("/remove/{id}")
       @PreAuthorize("hasAuthority('ADMIN')")
       public ResponseEntity<ApiResponse> removeProduct(@PathVariable Long id){
        ApiResponse<Void> apiResponse=productService.removeCategory(id);
        return ResponseEntity.ok(apiResponse);
       }
}
