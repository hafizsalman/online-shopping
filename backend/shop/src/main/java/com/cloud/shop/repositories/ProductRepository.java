package com.cloud.shop.repositories;

import com.cloud.shop.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository  extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingOrDescriptionContaining(String name, String description);

//    List<Product> findByCategoryAndPriceBetween(String category, double priceMin, double priceMax);
}
