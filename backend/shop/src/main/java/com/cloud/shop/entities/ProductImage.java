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
@Table(name="images_url")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    private Long productId;

    @Column(name = "image_url", length = 255)
    private String imageUrl; // Optional: Used for external URLs.

    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary;

    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;

    // Constructors
    public ProductImage() {}

    public ProductImage(Long product, String imageUrl, boolean isPrimary) {
        this.productId = product;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
        this.addedAt = LocalDateTime.now();
    }

    // Lifecycle Hook
    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}
