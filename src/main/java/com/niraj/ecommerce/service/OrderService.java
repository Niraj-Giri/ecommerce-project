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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        order.setOrderId(generateOrderNumber());

        Order savedOrder = orderRepository.save(order);

        // Clear cart after successful order
        cart.getItems().clear();
        cartRepository.save(cart);

        OrderResponse orderResponse = mapToOrderResponse(order);


        return new ApiResponse<>(
                true,
                "Order placed successfully",
                orderResponse
        );
    }

    public ApiResponse<OrderResponse> getOrderById(Long orderId, User user) {
        Order order=orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if(!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("User does not belong to order");
        }
        OrderResponse orderResponse = mapToOrderResponse(order);


        return new ApiResponse<>(true,"Order retrieved successfully",orderResponse);
    }

    public ApiResponse<Void> cancelOrder(Long orderId, User user) {
        Order order=orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if(!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("User does not belong to order");
        }
        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
        return new ApiResponse<>(true,"Order cancelled successfully",null);
    }

    public ApiResponse<List<OrderResponse>> getAllOrder(Long id) {
        List<OrderResponse> orderResponse=orderRepository.findByUserId(id)
                .stream()
                .map(this::mapToOrderResponse)
                .toList();

        return new ApiResponse<>(true,"Orders retrieved successfully",orderResponse);
    }

    public ApiResponse<List<OrderResponse>> getOrdersBystatus(String status, Long userId) {

        OrderStatus orderStatus;

        try {
            orderStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid order status: " + status);
        }

        List<Order> orders = orderRepository
                .findByUserIdAndOrderStatus(userId, orderStatus);

        List<OrderResponse> responses = orders.stream()
                .map(this::mapToOrderResponse)
                .toList();

        return new ApiResponse<>(
                true,
                "Orders retrieved successfully",
                responses
        );
    }

    public String generateOrderNumber() {
        String randomPart = UUID.randomUUID()
                .toString()
                .substring(0, 6)
                .toUpperCase();

        return "ORD-" +
                LocalDate.now().toString().replace("-", "") +
                "-" +
                randomPart;
    }
    private OrderResponse mapToOrderResponse(Order order) {

        return OrderResponse.builder()
                .id(order.getId())
                .orderId(order.getOrderId())
                .orderDate(order.getCreatedAt())
                .status(order.getOrderStatus().toString())
                .totalAmount(order.getTotalPrice())
                .build();
    }



}
