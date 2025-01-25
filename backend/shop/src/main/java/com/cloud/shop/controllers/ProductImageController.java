package com.cloud.shop.controllers;

import com.cloud.shop.entities.Product;
import com.cloud.shop.entities.ProductImage;
import com.cloud.shop.repositories.ProductImageRepository;
import com.cloud.shop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products/images")
public class ProductImageController {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam Long productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam boolean isPrimary) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            ProductImage productImage = new ProductImage();
            productImage.setProduct(product);
            productImage.setImageData(file.getBytes());
            productImage.isPrimary();
            productImageRepository.save(productImage);

            return ResponseEntity.ok("Image uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading image: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        ProductImage productImage = productImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") // Adjust MIME type as needed
                .body(productImage.getImageData());
    }

}
