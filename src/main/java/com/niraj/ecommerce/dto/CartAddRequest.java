package com.niraj.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartAddRequest {
     @NotNull
     private Long productId;
     @NotNull
     private Long quantity;
}
