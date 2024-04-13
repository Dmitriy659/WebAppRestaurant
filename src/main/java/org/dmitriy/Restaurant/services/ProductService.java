package org.dmitriy.Restaurant.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dmitriy.Restaurant.models.Image;
import org.dmitriy.Restaurant.models.Product;
import org.dmitriy.Restaurant.repositories.ImageRepository;
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
    private final ImageRepository imageRepository;

    public List<Product> allProducts(String category) {
        if (!category.equals("Все")) {
            return productRepository.findByCategory(category);
        }
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

    public void updateProduct(Long id, MultipartFile updateFile, Product updatedProduct) throws IOException {
        Product existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct != null) {

            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setPrice(updatedProduct.getPrice());

            if (updateFile != null && updateFile.getSize() != 0) {
                Image image = toImageEntity(updateFile);
                if (existingProduct.getImage() != null) {
                    imageRepository.deleteById(existingProduct.getImage().getId());
                }
                existingProduct.addImage(image);
            }
            productRepository.save(existingProduct);
        }
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
