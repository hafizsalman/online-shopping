package com.cloud.shop.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@Entity
@Table(name = "product_image")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "image_url", length = 255)
    private String imageUrl; // Optional: Used for external URLs.

    @Lob
    @Column(name = "image_data", columnDefinition = "BYTEA") // Use BYTEA for PostgreSQL
    private byte[] imageData;
    
    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary;

    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;

    // Constructors
    public ProductImage() {}

    public ProductImage(Product product, String imageUrl, byte[] imageData, boolean isPrimary) {
        this.product = product;
        this.imageUrl = imageUrl;
        this.imageData = imageData;
        this.isPrimary = isPrimary;
        this.addedAt = LocalDateTime.now();
    }

    // Lifecycle Hook
    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }
}
