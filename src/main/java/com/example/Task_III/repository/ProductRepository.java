package com.example.Task_III.repository;

import com.example.Task_III.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByQuantityLessThan(int threshold);

    @Query("SELECT p FROM Product p WHERE p.category.categoryName LIKE %?1% OR p.productName LIKE %?2%")
    List<Product> filterProducts(String categoryName, String productName);
}
