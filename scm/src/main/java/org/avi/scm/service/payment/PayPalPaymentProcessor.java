package org.avi.scm.service.payment;

import org.springframework.stereotype.Component;

@Component
public class PayPalPaymentProcessor implements IPaymentProcessor {

    @Override
    public void process(Long orderId, double amount) {
        System.out.println("Processing PayPal payment for order " + orderId + " amount " + amount);
    }

    @Override
    public String getMode() {
        return "PAYPAL";
    }
}

