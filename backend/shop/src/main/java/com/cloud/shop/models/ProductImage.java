package com.cloud.shop.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_image")
public class ProductImage {

    // Getters and Setters
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Setter
    @Getter
    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary;

    @Setter
    @Getter
    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;

    // Constructors
    public ProductImage() {
    }

    public ProductImage(Product product, String imageUrl, boolean isPrimary) {
        this.product = product;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
        this.addedAt = LocalDateTime.now();
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    // Lifecycle Hook
    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }
}
