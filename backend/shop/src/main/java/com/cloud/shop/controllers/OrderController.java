package com.cloud.shop.controllers;

import com.cloud.shop.entities.Order;
import com.cloud.shop.services.EmailService;
import com.cloud.shop.services.InventoryService;
import com.cloud.shop.services.OrderService;
import com.cloud.shop.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        boolean paymentSuccessful = paymentService.processPayment();
        if (!paymentSuccessful) {
            return ResponseEntity.badRequest().body(null);
        }

        order.getOrderItems().forEach(orderItem -> {
            inventoryService.updateStock(orderItem.getProduct().getProductId(), orderItem.getQuantity());
        });
        Order savedOrder = orderService.placeOrder(order);
        emailService.sendOrderConfirmation("customer@example.com", "Order Confirmation", "Your order has been placed successfully.");
        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
