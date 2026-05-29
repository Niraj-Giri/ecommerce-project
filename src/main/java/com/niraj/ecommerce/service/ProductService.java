package com.niraj.ecommerce.service;

import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.dto.ProductAddRequest;
import com.niraj.ecommerce.dto.ProductResponse;
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
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ApiResponse<List<ProductResponse>> findByCategoryId(Long id) {
        List<ProductResponse> productResponses = productRepository.findByCategoryId(id)
                .stream()
                .map(ProductResponse::new)
                .toList();

        return new ApiResponse<>(true,"Products fetched successfully", productResponses);
    }

    public ApiResponse<Void> addProduct(ProductAddRequest productAddRequest) {
        Product product = new Product();
        product.setName(productAddRequest.getName());
        product.setMrp(productAddRequest.getMrp());
        product.setPrice(productAddRequest.getPrice());
        product.setImageUrl(productAddRequest.getImageUrl());
        product.setDescription(productAddRequest.getDescription());
        product.setQuantity(product.getQuantity());
        product.setSlug(productAddRequest.getSlug());
        product.setCategory(categoryRepository.findById(productAddRequest.getCategoryId()));

    }
}
