package org.dmitriy.Restaurant.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dmitriy.Restaurant.models.Image;
import org.dmitriy.Restaurant.models.Product;
import org.dmitriy.Restaurant.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public void saveProduct(Product product, MultipartFile file) throws IOException {
        Image image;
        if (file.getSize() != 0) {
            image = toImageEntity(file);
            product.addImage(image);
            System.out.println("картинка");
        }
        log.info("Saving new Product");
        productRepository.save(product);
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
