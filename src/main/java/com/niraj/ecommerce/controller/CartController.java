package com.niraj.ecommerce.controller;


import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.dto.CartAddRequest;
import com.niraj.ecommerce.dto.CartResponse;
import com.niraj.ecommerce.model.Cart;
import com.niraj.ecommerce.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

     private final CartService cartService;

     public CartController(CartService cartService) {
         this.cartService = cartService;
     }

     @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CartResponse>> getCart(@PathVariable Long id) {
         ApiResponse<CartResponse> cartResponse=cartService.getCart(id);
         return new ResponseEntity<>(cartResponse, HttpStatus.OK);
     }
     @PostMapping("/add/{userId}")
    public ResponseEntity<ApiResponse<Void>> addToCart(@PathVariable Long userId, @RequestBody CartAddRequest  cartAddRequest) {
         ApiResponse<Void> apiResponse=cartService.addToCart(userId,cartAddRequest);
         return new ResponseEntity<>(apiResponse, HttpStatus.OK);
     }
}
