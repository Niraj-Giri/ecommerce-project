package com.niraj.ecommerce.dto;

import com.niraj.ecommerce.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private Long parentId;
    private String slug;



    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.imageUrl = category.getImageUrl();
        this.slug = category.getSlug();
        this.parentId = category.getParent() != null
                ? category.getParent().getId()
                : null;
    }
}
