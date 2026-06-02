package com.niraj.ecommerce.repository;

import com.niraj.ecommerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    Address findByUserId(Long userId);
}
