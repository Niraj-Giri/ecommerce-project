package com.niraj.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    private String orderId;
    private Long id;
    private String status;
    private Double totalAmount;
    private LocalDateTime orderDate;
}