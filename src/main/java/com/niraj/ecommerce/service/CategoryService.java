package com.niraj.ecommerce.service;

import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.dto.CategoryAddRequest;
import com.niraj.ecommerce.dto.CategoryResponse;
import com.niraj.ecommerce.exception.ResourceNotFoundException;
import com.niraj.ecommerce.model.Category;
import com.niraj.ecommerce.repository.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> listOfCategories = categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::new)
                .toList();

        return new ApiResponse<>(
                true,
                "Categories fetched successfully",
                listOfCategories
        );
    }

    public ApiResponse<CategoryResponse> getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        CategoryResponse response = new CategoryResponse(category);

        return new ApiResponse<>(
                true,
                "Category fetched successfully",
                response
        );
    }
    public ApiResponse<Void> addCategory(CategoryAddRequest categoryAddRequest) {
        Category category = new Category();
        category.setName(categoryAddRequest.getName());
        category.setSlug(categoryAddRequest.getSlug());
        category.setImageUrl(categoryAddRequest.getImageUrl());
        if(categoryAddRequest.getParentId()!=null){
            category.setParent(categoryRepository.findById(categoryAddRequest.getParentId())

                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found")));
        }
        categoryRepository.save(category);
        return new ApiResponse<>(true,"Category added successfully",null);
    }
    public ApiResponse<Void> updateCategory(CategoryAddRequest categoryAddRequest,Long id) {
         Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
         category.setName(categoryAddRequest.getName());
         category.setSlug(categoryAddRequest.getSlug());
         category.setImageUrl(categoryAddRequest.getImageUrl());
         if(categoryAddRequest.getParentId()!=null){
             category.setParent(categoryRepository.findById(categoryAddRequest.getParentId())

                     .orElseThrow(() -> new ResourceNotFoundException("Parent category not found")));
         }
         categoryRepository.save(category);
         return new ApiResponse<>(true,"Category updated successfully",null);
    }

    public ApiResponse<Void> removeCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setActive(false);
        categoryRepository.save(category);
        return new ApiResponse<>(true,"Category removed successfully",null);
    }
}
