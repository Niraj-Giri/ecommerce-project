package com.niraj.ecommerce.controller;

import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.dto.CartAddRequest;
import com.niraj.ecommerce.dto.CartResponse;
import com.niraj.ecommerce.dto.CartUpdateRequest;
import com.niraj.ecommerce.exception.UnauthorizedAccessException;
import com.niraj.ecommerce.model.User;
import com.niraj.ecommerce.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

     private final CartService cartService;

     public CartController(CartService cartService) {
         this.cartService = cartService;
     }

     @GetMapping("/{userId}")
     public ResponseEntity<ApiResponse<CartResponse>> getCart(@PathVariable Long userId) {
         User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         if (!user.getId().equals(userId)) {
             throw new UnauthorizedAccessException("You are not authorized to view this cart.");
         }
         ApiResponse<CartResponse> cartResponse = cartService.getCart(userId);
         return new ResponseEntity<>(cartResponse, HttpStatus.OK);
     }

     @PostMapping("/add/{userId}")
     public ResponseEntity<ApiResponse<Void>> addToCart(@PathVariable Long userId, @RequestBody CartAddRequest cartAddRequest) {
         User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         if (!user.getId().equals(userId)) {
             throw new UnauthorizedAccessException("You are not authorized to modify this cart.");
         }
         ApiResponse<Void> apiResponse = cartService.addToCart(userId, cartAddRequest);
         return new ResponseEntity<>(apiResponse, HttpStatus.OK);
     }

     @PatchMapping("/update/quantity/{cartItemId}")
     public ResponseEntity<ApiResponse<Void>> updateQuantity(@PathVariable Long cartItemId, @RequestBody CartUpdateRequest cartUpdateRequest) {
         User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         ApiResponse<Void> apiResponse = cartService.updateQuantity(cartItemId, user.getId(), cartUpdateRequest);
         return new ResponseEntity<>(apiResponse, HttpStatus.OK);
     }

     @DeleteMapping("/remove/{userId}")
     public ResponseEntity<ApiResponse<Void>> removeCart(@PathVariable Long userId) {
         User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         if (!user.getId().equals(userId)) {
             throw new UnauthorizedAccessException("You are not authorized to remove this cart.");
         }
         ApiResponse<Void> apiResponse = cartService.removeCart(userId);
         return new ResponseEntity<>(apiResponse, HttpStatus.OK);
     }

     
}
