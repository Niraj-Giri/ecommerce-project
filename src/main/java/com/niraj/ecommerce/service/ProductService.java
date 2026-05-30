package com.niraj.ecommerce.service;

import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.dto.ProductAddRequest;
import com.niraj.ecommerce.dto.ProductResponse;
import com.niraj.ecommerce.exception.ResourceNotFoundException;
import com.niraj.ecommerce.model.Category;
import com.niraj.ecommerce.model.Product;
import com.niraj.ecommerce.repository.CategoryRepository;
import com.niraj.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    public ProductService(ProductRepository productRepository , CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository=categoryRepository;
    }

    public ApiResponse<List<ProductResponse>> findByCategoryId(Long id) {
        List<ProductResponse> productResponses = productRepository.findByCategoryId(id)
                .stream()
                .map(ProductResponse::new)
                .toList();

        return new ApiResponse<>(true,"Products fetched successfully", productResponses);
    }

    public ApiResponse<Void> addProduct(ProductAddRequest productAddRequest ,Long id) {
        Product product = new Product();
        product.setName(productAddRequest.getName());
        product.setMrp(productAddRequest.getMrp());
        product.setPrice(productAddRequest.getPrice());
        product.setImageUrl(productAddRequest.getImageUrl());
        product.setDescription(productAddRequest.getDescription());
        product.setQuantity(productAddRequest.getQuantity());
        product.setSlug(productAddRequest.getSlug());
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        product.setCategory(category);
        productRepository.save(product);
        return new ApiResponse<>(true,"Product added successfully",null);

    }

    public ApiResponse<Void> updateCategory(ProductAddRequest productAddRequest, Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setName(productAddRequest.getName());
        product.setMrp(productAddRequest.getMrp());
        product.setPrice(productAddRequest.getPrice());
        product.setImageUrl(productAddRequest.getImageUrl());
        product.setDescription(productAddRequest.getDescription());
        product.setQuantity(productAddRequest.getQuantity());
        product.setSlug(productAddRequest.getSlug());
       productRepository.save(product);
       return new ApiResponse<>(true,"Product updated successfully",null);
    }

    public ApiResponse<Void> removeCategory(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
        return new ApiResponse<>(true,"Product removed successfully",null);
    }
}
