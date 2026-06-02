package com.niraj.ecommerce.service;

import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.dto.ProductAddRequest;
import com.niraj.ecommerce.dto.ProductResponse;
import com.niraj.ecommerce.exception.ResourceNotFoundException;
import com.niraj.ecommerce.model.Category;
import com.niraj.ecommerce.model.Product;
import com.niraj.ecommerce.repository.CategoryRepository;
import com.niraj.ecommerce.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    public ProductService(ProductRepository productRepository , CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository=categoryRepository;
    }

    public ApiResponse<Page<ProductResponse>> findByCategoryId(
            Long categoryId,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ProductResponse> products =
                productRepository.findByCategoryId(categoryId, pageable)
                        .map(ProductResponse::new);

        return new ApiResponse<>(
                true,
                "Products fetched successfully",
                products
        );
    }

    public ApiResponse<ProductResponse> addProduct(ProductAddRequest productAddRequest ,Long id) {
        log.info("Adding new product with name: {} to category ID: {}", productAddRequest.getName(), id);
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
        Product saved = productRepository.save(product);
        return new ApiResponse<>(true, "Product added successfully", new ProductResponse(saved));

    }

    public ApiResponse<ProductResponse> updateCategory(ProductAddRequest productAddRequest, Long id) {
        log.info("Updating product ID: {} with name: {}", id, productAddRequest.getName());
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setName(productAddRequest.getName());
        product.setMrp(productAddRequest.getMrp());
        product.setPrice(productAddRequest.getPrice());
        product.setImageUrl(productAddRequest.getImageUrl());
        product.setDescription(productAddRequest.getDescription());
        product.setQuantity(productAddRequest.getQuantity());
        product.setSlug(productAddRequest.getSlug());
        Product saved = productRepository.save(product);
        return new ApiResponse<>(true, "Product updated successfully", new ProductResponse(saved));
    }

    public ApiResponse<Void> removeCategory(Long id) {
        log.info("Removing/Deactivating product ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
        return new ApiResponse<>(true,"Product removed successfully",null);
    }

    public ApiResponse<ProductResponse> getProductById(Long productId) {

        ProductResponse productResponse = productRepository.findById(productId)
                .map(ProductResponse::new)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        return new ApiResponse<>(
                true,
                "Product fetched successfully",
                productResponse
        );
    }

    public ApiResponse<Page<ProductResponse>> getProductsByCategory(
            Long categoryId,
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort;

        switch (sortBy) {
            case "price_asc":
                sort = Sort.by("price").ascending();
                break;

            case "price_desc":
                sort = Sort.by("price").descending();
                break;

            case "discount_asc":
                sort = Sort.by("discountPercentage").ascending();
                break;

            case "discount_desc":
                sort = Sort.by("discountPercentage").descending();
                break;

            default:
                sort = Sort.by("id").descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProductResponse> products = productRepository
                .findByCategoryId(categoryId, pageable)
                .map(ProductResponse::new);

        return new ApiResponse<>(
                true,
                "Products fetched successfully",
                products
        );
    }

    public ApiResponse<String> getImageById(Long productId) {
        Product product=productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return new ApiResponse<>(true,"image fetched successfully",product.getImageUrl());
    }
}
