package com.cloud.shop.controllers;

import com.cloud.shop.entities.Product;
import com.cloud.shop.repositories.ProductImageRepository;
import com.cloud.shop.repositories.ProductRepository;
import com.cloud.shop.s3.S3ImageService;
import com.cloud.shop.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "Operations related to products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private S3ImageService s3ImageService;

    @GetMapping
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();

        // Convert stored image paths to Pre-Signed URLs
        List<Product> updatedProducts = products.stream().map(product -> {
            if (product.getImageUrl() != null) {
                String objectKey = product.getImageUrl().replace("https://lowtech-webshop-dev.s3.eu-central-1.amazonaws.com/", "");
                product.setImageUrl(s3ImageService.generatePreSignedUrl(objectKey));
            }
            return product;
        }).collect(Collectors.toList());

        return updatedProducts;

    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Replace static S3 URL with Pre-Signed URL
        if (product.getImageUrl() != null) {
            String objectKey = product.getImageUrl().replace("https://lowtech-webshop-dev.s3.eu-central-1.amazonaws.com/", "");
            product.setImageUrl(s3ImageService.generatePreSignedUrl(objectKey));
        }

        return ResponseEntity.ok(product);
    }


    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        try {
            return ResponseEntity.ok(productService.updateProduct(id, productDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{productId}/upload-image")
    public ResponseEntity<String> uploadProductImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam boolean isPrimary) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        String imageUrl = s3ImageService.uploadImage(file);

        product.setImageUrl(imageUrl);
        productRepository.save(product);

        return ResponseEntity.ok("Image uploaded successfully. URL: " + imageUrl);
    }

    // Utility method to replace static S3 URLs with Pre-Signed URLs
    private List<Product> replaceImageUrlsWithPreSigned(List<Product> products) {
        return products.stream().map(product -> {
            if (product.getImageUrl() != null) {
                String objectKey = product.getImageUrl().replace("https://lowtech-webshop-dev.s3.eu-central-1.amazonaws.com/", "");
                product.setImageUrl(s3ImageService.generatePreSignedUrl(objectKey));
            }
            return product;
        }).collect(Collectors.toList());
    }

    // ✅ Updated Search Endpoint with Pre-Signed URLs
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        return ResponseEntity.ok(replaceImageUrlsWithPreSigned(products));
    }

    // ✅ Updated Filter Endpoint with Pre-Signed URLs
    @GetMapping("/filter")
    public ResponseEntity<List<Product>> filterProducts(
            @RequestParam String category,
            @RequestParam double priceMin,
            @RequestParam double priceMax) {
        List<Product> products = productService.filterProducts(category, priceMin, priceMax);
        return ResponseEntity.ok(replaceImageUrlsWithPreSigned(products));
    }

}