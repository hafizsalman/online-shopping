package com.cloud.shop.services;

import com.cloud.shop.entities.Product;
import com.cloud.shop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id).map(product -> {
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setStockQuantity(productDetails.getStockQuantity());
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }


    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingOrDescriptionContaining(keyword, keyword);
    }

    public List<Product> filterProducts(String category, double priceMin, double priceMax) {
        return List.of();// productRepository.findByCategoryAndPriceBetween(category, priceMin, priceMax);
    }
}