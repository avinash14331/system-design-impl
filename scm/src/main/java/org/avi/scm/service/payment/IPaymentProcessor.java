package org.avi.scm.service.payment;

public interface IPaymentProcessor {
    void process(Long orderId, double amount);
    String getMode(); // returns mode name for lookup
}

