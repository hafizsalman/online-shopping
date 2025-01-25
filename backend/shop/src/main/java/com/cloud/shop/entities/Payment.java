package com.cloud.shop.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Setter
    @Getter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Setter
    @Getter
    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod; //TODO: Use ENUM ENUM('Credit Card', 'PayPal', 'Stripe', 'Mock')

    @Setter
    @Getter
    @Column(name = "payment_status", nullable = false, length = 20)
    private String paymentStatus;

    @Setter
    @Getter
    @Column(name = "transaction_id", unique = true, length = 255)
    private String transactionId;

    @Setter
    @Getter
    @Column(name = "amount", nullable = false)
    private Double amount;

    @Setter
    @Getter
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

