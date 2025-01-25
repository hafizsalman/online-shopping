package com.cloud.shop.services;

import com.cloud.shop.entities.Inventory;
import com.cloud.shop.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public Inventory updateStock(Long productId, int quantity) {
        return inventoryRepository.findById(productId).map(inventory -> {
            if (inventory.getQuantity() != null) {
                inventory.setQuantity(inventory.getQuantity() - quantity);
                if (inventory.getQuantity() < inventory.getRestockThreshold()) {
                    System.out.println("Warning: Stock is low for product ID: " + productId);
                }
                return inventoryRepository.save(inventory);
            } else {
                throw new RuntimeException("Stock information is missing for product ID: " + productId);
            }
        }).orElseThrow(() -> new RuntimeException("Product not found with id " + productId));
    }
}