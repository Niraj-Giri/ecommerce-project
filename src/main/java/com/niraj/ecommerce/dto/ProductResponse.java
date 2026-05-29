package com.niraj.ecommerce.dto;

import com.niraj.ecommerce.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
     private Long id;
     private String name;
     private String description;
     private Double price;
     private String imageUrl;
     private Long categoryId;
     private String slug;
     private Double mrp;

    public ProductResponse(Product product) {
          this.id = product.getId();
          this.name = product.getName();
          this.description=product.getDescription();
          this.categoryId=product.getCategory().getId();
          this.slug=product.getSlug();
          this.imageUrl=product.getImageUrl();
          this.price=product.getPrice();
          this.mrp=product.getMrp();

     }


}
