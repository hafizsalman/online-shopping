package com.cloud.shop.controllers;

import com.cloud.shop.entities.Cart;
import com.cloud.shop.entities.CartRequest;
import com.cloud.shop.entities.Customer;
import com.cloud.shop.entities.Product;
import com.cloud.shop.repositories.CustomerRepository;
import com.cloud.shop.repositories.ProductRepository;
import com.cloud.shop.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/add")
    public ResponseEntity<Cart> addItemToCart(@RequestBody CartRequest cartRequest) {
        Customer customer = customerRepository.findById(cartRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Product product = productRepository.findById(cartRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setProduct(product);
        cart.setQuantity(cartRequest.getQuantity());
        cart.setStatus("Active"); // Set default status

        Cart savedCart = cartService.addItemToCart(cart);
        return ResponseEntity.ok(savedCart);
    }
    @GetMapping
    public ResponseEntity<Cart> viewCart(@RequestParam Long cartId) {
        Optional<Cart> cart = cartService.getCartById(cartId);
        return cart.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Long id) {
        cartService.removeItemFromCart(id);
        return ResponseEntity.noContent().build();
    }
}