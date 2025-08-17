package org.avi.scm.service.payment;

import lombok.RequiredArgsConstructor;
import org.avi.scm.entity.Payment;
import org.avi.scm.repository.PaymentRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final List<IPaymentProcessor> processors;
    private final PaymentRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void processPayment(Long orderId, double amount, String mode) {
        IPaymentProcessor processor = processors.stream()
                .filter(p -> p.getMode().equalsIgnoreCase(mode))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Payment mode not supported: " + mode));

        processor.process(orderId, amount);
        Payment payment = repository.save(new Payment(null, orderId, amount, "COMPLETED"));
        // Publish event for Saga Orchestrator
        kafkaTemplate.send("payment-events", "PAYMENT_COMPLETED:" + orderId);
    }

    public void processPayment(Long orderId, double amount) {
        Payment payment = repository.save(new Payment(null, orderId, amount, "COMPLETED"));
        kafkaTemplate.send("payment-events", "PAYMENT_COMPLETED:" + orderId);
    }

    public void refundPayment(Long orderId) {
        repository.findByOrderId(orderId).ifPresent(payment -> {
            payment.setStatus("REFUNDED");
            repository.save(payment);
        });
    }
}

