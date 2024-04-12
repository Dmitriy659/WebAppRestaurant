package org.dmitriy.Restaurant.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dmitriy.Restaurant.models.Product;
import org.dmitriy.Restaurant.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> allProducts() {
        return productRepository.findAll();
    }

    public Product getOneProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
