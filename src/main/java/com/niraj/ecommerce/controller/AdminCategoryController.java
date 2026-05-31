package com.niraj.ecommerce.controller;

import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.dto.CategoryAddRequest;
import com.niraj.ecommerce.dto.CategoryResponse;
import com.niraj.ecommerce.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
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
    public ResponseEntity<ApiResponse> addCategory(@RequestBody CategoryAddRequest categoryAddRequest){
        ApiResponse<Void> apiResponse=categoryService.addCategory(categoryAddRequest);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("updateCategory/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody CategoryAddRequest categoryAddRequest ,@PathVariable Long id){
        ApiResponse<Void> apiResponse=categoryService.updateCategory(categoryAddRequest,id);
        return ResponseEntity.ok(apiResponse);
    }
    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> removeCategory(@PathVariable Long id){
        ApiResponse<Void> apiResponse=categoryService.removeCategory(id);
        return ResponseEntity.ok(apiResponse);
    }
}
