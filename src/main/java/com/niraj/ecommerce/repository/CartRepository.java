package com.niraj.ecommerce.repository;

import com.niraj.ecommerce.model.Cart;
import com.niraj.ecommerce.model.CartItem;
import com.niraj.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
     Cart findById(long cartId);
     Cart findByUserId(Long userId);

    Long user(User user);
}
