package org.dmitriy.Restaurant.repositories;

import org.dmitriy.Restaurant.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
