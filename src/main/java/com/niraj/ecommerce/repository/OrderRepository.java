package com.niraj.ecommerce.repository;

import com.niraj.ecommerce.model.Order;
import com.niraj.ecommerce.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Order findById(long id);

   List<Order> findByUserId(Long id);
   List<Order> findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);
}
