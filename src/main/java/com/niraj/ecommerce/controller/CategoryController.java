package com.niraj.ecommerce.controller;

import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.dto.CategoryResponse;
import com.niraj.ecommerce.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/category")
public class CategoryController {

    private final CategoryService categoryService;
    public  CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping
    ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories(){
        ApiResponse<List<CategoryResponse>> listOfCategories=categoryService.getAllCategories();
        return ResponseEntity.ok(listOfCategories);
    }
}
