package com.cloud.shop.services;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class PaymentService {

    public boolean processPayment() {
        Random random = new Random();
        return random.nextBoolean();
    }
}
