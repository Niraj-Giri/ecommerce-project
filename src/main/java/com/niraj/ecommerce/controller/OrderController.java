package com.niraj.ecommerce.controller;

import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.dto.OrderResponse;
import com.niraj.ecommerce.dto.PlaceOrderRequest;
import com.niraj.ecommerce.model.Order;
import com.niraj.ecommerce.model.User;
import com.niraj.ecommerce.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
     public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable Long id) {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApiResponse<OrderResponse> orderResponse=orderService.getOrderById(id,user);
        return ResponseEntity.ok(orderResponse);
    }

    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable("orderId") Long orderId) {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApiResponse<Void> apiResponse=orderService.cancelOrder(orderId,user);
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApiResponse<List<OrderResponse>> orderResponse=orderService.getAllOrder(user.getId());
        return ResponseEntity.ok(orderResponse);

    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByStatus(@RequestParam String status){
         User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         ApiResponse<List<OrderResponse>> orderResponse=orderService.getOrdersBystatus(status,user.getId());
         return ResponseEntity.ok(orderResponse);
    }

}
