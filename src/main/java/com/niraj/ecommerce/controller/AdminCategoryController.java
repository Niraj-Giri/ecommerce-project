package com.niraj.ecommerce.controller;

import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.dto.CategoryAddRequest;
import com.niraj.ecommerce.dto.CategoryResponse;
import com.niraj.ecommerce.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/category")
public class AdminCategoryController {

    private final CategoryService categoryService;
    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    ResponseEntity<ApiResponse<List<CategoryResponse>>>  getAllCategories(){
        ApiResponse<List<CategoryResponse>> listOfCategories=categoryService.getAllCategories();
        return ResponseEntity.ok(listOfCategories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id){
        ApiResponse<CategoryResponse> category=categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponse>> addCategory(@RequestBody CategoryAddRequest categoryAddRequest){
        ApiResponse<CategoryResponse> apiResponse = categoryService.addCategory(categoryAddRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping("updateCategory/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@RequestBody CategoryAddRequest categoryAddRequest ,@PathVariable Long id){
        ApiResponse<CategoryResponse> apiResponse = categoryService.updateCategory(categoryAddRequest, id);
        return ResponseEntity.ok(apiResponse);
    }
    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> removeCategory(@PathVariable Long id){
        ApiResponse<Void> apiResponse=categoryService.removeCategory(id);
        return ResponseEntity.ok(apiResponse);
    }
}
