package com.niraj.ecommerce.repository;
import com.niraj.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Product findById(long id);
    Product findByName(String name);
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);


}
