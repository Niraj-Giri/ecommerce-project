package com.niraj.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryAddRequest {
    @NotBlank private String name;
    @NotBlank private String slug;
    @NotBlank  private String imageUrl;
    private Long parentId;
}
