package com.niraj.ecommerce.service;

import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.dto.OrderResponse;
import com.niraj.ecommerce.dto.PlaceOrderRequest;
import com.niraj.ecommerce.exception.ResourceNotFoundException;
import com.niraj.ecommerce.model.*;
import com.niraj.ecommerce.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;
    public OrderService(OrderRepository orderRepository,CartRepository cartRepository,
                        UserRepository userRepository,AddressRepository addressRepository,
                        OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository=cartRepository;
        this.userRepository=userRepository;
        this.addressRepository=addressRepository;
        this.orderItemRepository=orderItemRepository;
    };

    @Transactional

    public ApiResponse<OrderResponse> placeOrder(PlaceOrderRequest placeOrderRequest, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }

        List<CartItem> cartItems = cart.getItems();

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Address address = addressRepository.findById(placeOrderRequest.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Address does not belong to user");
        }

        // Create address snapshot
        OrderAddress orderAddress = new OrderAddress();
        orderAddress.setFullName(address.getFullName());
        orderAddress.setCity(address.getCity());
        orderAddress.setState(address.getState());
        orderAddress.setZipcode(address.getZipcode());
        orderAddress.setMobile(address.getMobile());
        orderAddress.setHouse(address.getHouse());

        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PLACED);
        order.setOrderAddress(orderAddress);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (CartItem cartItem : cartItems) {

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());

            double priceAtPurchase = cartItem.getProduct().getPrice();

            orderItem.setPriceAtPurchase(priceAtPurchase);

            totalPrice += priceAtPurchase * cartItem.getQuantity();

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        // Clear cart after successful order
        cart.getItems().clear();
        cartRepository.save(cart);

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(savedOrder.getId());
        orderResponse.setStatus(savedOrder.getOrderStatus().name());
        orderResponse.setTotalAmount(savedOrder.getTotalPrice());
        orderResponse.setOrderDate(savedOrder.getCreatedAt());

        return new ApiResponse<>(
                true,
                "Order placed successfully",
                orderResponse
        );
    }
}
