package com.niraj.ecommerce.repository;

import com.niraj.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 1. Used for Login & Security
    Optional<User> findByEmail(String email);

    // 2. Used for Registration Validation
    boolean existsByEmail(String email);

}