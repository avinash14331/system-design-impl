package org.avi.scm.service.payment;

import org.springframework.stereotype.Component;

@Component
public class CreditCardPaymentProcessor implements IPaymentProcessor {

    @Override
    public void process(Long orderId, double amount) {
        // Business logic for credit card payment
        System.out.println("Processing Credit Card payment for order " + orderId + " amount " + amount);
        // Simulate success/failure
    }

    @Override
    public String getMode() {
        return "CREDIT_CARD";
    }
}

