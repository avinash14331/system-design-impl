package org.avi.scm.service;

import lombok.RequiredArgsConstructor;
import org.avi.scm.entity.Order;
import org.avi.scm.service.payment.PaymentService;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@KafkaListener(topics = {"order-events", "payment-events", "inventory-events"})
public class SagaOrchestrator {
    private final PaymentService paymentService;
    private final InventoryService inventoryService;
    private final OrderService orderService;

    @KafkaHandler
    public void handle(String message) {
        String[] parts = message.split(":");
        String event = parts[0];
        Long orderId = Long.valueOf(parts[1]);

        switch (event) {
            case "ORDER_CREATED":
                Order order = orderService.getOrder(orderId);
                if(order == null){
                    orderService.cancelOrder(orderId);
                }
                String mode = order.getPaymentMode() != null ? order.getPaymentMode() : "CREDIT_CARD";
                paymentService.processPayment(orderId, 100.0, mode);
                break;
            case "PAYMENT_COMPLETED":
                inventoryService.reserveStock(orderId, "PRODUCT_X", 1);
                break;
            case "STOCK_RESERVED":
                orderService.confirmOrder(orderId);
                break;
            case "STOCK_FAILED":
                paymentService.refundPayment(orderId);
                orderService.cancelOrder(orderId);
                break;
        }
    }
}

