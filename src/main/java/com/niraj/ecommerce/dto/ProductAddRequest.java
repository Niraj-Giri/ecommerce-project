package com.niraj.ecommerce.dto;

import lombok.Data;

@Data
public class ProductAddRequest {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private Long categoryId;
    private String slug;
    private Long quantity;
    private  Double mrp;
}
