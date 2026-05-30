package com.niraj.ecommerce.service;

import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.dto.CartAddRequest;
import com.niraj.ecommerce.dto.CartItemResponse;
import com.niraj.ecommerce.dto.CartResponse;
import com.niraj.ecommerce.exception.ResourceNotFoundException;
import com.niraj.ecommerce.model.Cart;
import com.niraj.ecommerce.model.CartItem;
import com.niraj.ecommerce.model.Product;
import com.niraj.ecommerce.repository.CartItemRepository;
import com.niraj.ecommerce.repository.CartRepository;
import com.niraj.ecommerce.repository.ProductRepository;
import com.niraj.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository  cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository  productRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                       UserRepository userRepository,ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public ApiResponse<CartResponse> getCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            return new ApiResponse<>(false, "No cart found", null);
        }

        List<CartItem> cartItems =
                cartItemRepository.findByCartId(cart.getId());

        List<CartItemResponse> itemResponses = cartItems.stream()
                .map(item -> {
                    CartItemResponse response = new CartItemResponse();

                    response.setId(item.getId());
                    response.setProductId(item.getProduct().getId());
                    response.setQuantity(item.getQuantity());

                    return response;
                })
                .toList();

        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartId(cart.getId());
        cartResponse.setItems(itemResponses);

        return new ApiResponse<>(true, "Cart fetched successfully", cartResponse);
    }

    public ApiResponse<Void> addToCart(Long userId, CartAddRequest cartAddRequest) {

        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {

            cart = new Cart();
            cart.setUser(
                    userRepository.findById(userId)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException("User not found"))
            );

            cart = cartRepository.save(cart);
        }

        Product product = productRepository.findById(cartAddRequest.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));
        Optional<CartItem> existingCartItem =
                cartItemRepository.findByCartIdAndProductId(
                        cart.getId(),
                        product.getId()
                );

        if (existingCartItem.isPresent()) {

            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(
                    cartItem.getQuantity() + cartAddRequest.getQuantity()
            );

            cartItemRepository.save(cartItem);

        } else {

            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartAddRequest.getQuantity());

            cartItemRepository.save(cartItem);
        }

        return new ApiResponse<>(
                true,
                "Product added to cart successfully",
                null
        );
    }

}
