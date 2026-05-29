package com.niraj.ecommerce.controller;

import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.dto.ProductAddRequest;
import com.niraj.ecommerce.dto.ProductResponse;
import com.niraj.ecommerce.model.Product;
import com.niraj.ecommerce.repository.ProductRepository;
import com.niraj.ecommerce.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product")
public class ProductController {

       private final ProductService productService;
       public ProductController(ProductService productService) {
              this.productService = productService;
       }

        @GetMapping("/category/{id}")
        public ResponseEntity<ApiResponse<List<ProductResponse>>> findByCategoryId(@PathVariable  Long id) {

            ApiResponse<List<ProductResponse>> productList=productService.findByCategoryId(id);
            return ResponseEntity.ok(productList);
        }
        @PostMapping("/category/add/{id}")
         public ResponseEntity<ApiResponse> addProduct(@PathVariable  Long id, @RequestBody ProductAddRequest  productAddRequest) {
           ApiResponse<Void> apiResponse=productService.addProduct(productAddRequest);
           return ResponseEntity.ok(apiResponse);
        }
}
