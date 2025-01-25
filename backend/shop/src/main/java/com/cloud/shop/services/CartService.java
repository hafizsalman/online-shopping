package com.cloud.shop.services;

import com.cloud.shop.entities.Cart;
import com.cloud.shop.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public Cart addItemToCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public Optional<Cart> getCartById(Long id) {
        return cartRepository.findById(id);
    }

    public void removeItemFromCart(Long id) {
        cartRepository.deleteById(id);
    }
}