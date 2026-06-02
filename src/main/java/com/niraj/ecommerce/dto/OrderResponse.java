package com.niraj.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long orderId;
    private String status;
    private Double totalAmount;
    private LocalDateTime orderDate;
}