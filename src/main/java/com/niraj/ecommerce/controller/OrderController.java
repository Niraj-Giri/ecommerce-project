package com.niraj.ecommerce.controller;

import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.dto.OrderResponse;
import com.niraj.ecommerce.dto.PlaceOrderRequest;
import com.niraj.ecommerce.model.User;
import com.niraj.ecommerce.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(@RequestBody PlaceOrderRequest placeOrderRequest) {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApiResponse<OrderResponse> orderResponse=orderService.placeOrder(placeOrderRequest,user.getId());
        return ResponseEntity.ok(orderResponse);
    }

}
