package org.avi.scm.service.payment;

import org.springframework.stereotype.Component;

@Component
public class UpiPaymentProcessor implements IPaymentProcessor {

    @Override
    public void process(Long orderId, double amount) {
        System.out.println("Processing UPI payment for order " + orderId + " amount " + amount);
    }

    @Override
    public String getMode() {
        return "UPI";
    }
}

