package com.niraj.ecommerce.repository;

import com.niraj.ecommerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    Address findByUserId(Long userId);
    List<Address> findAllByUserId(Long userId);
}
