package org.avi.scm.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.avi.scm.entity.Order;
import org.avi.scm.repository.OrderRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public Order createOrder(String product, int quantity, String paymentMode) {
        Order order = repository.save(new Order(null, product, quantity, "CREATED", paymentMode));
        // publish event to orchestrator
        kafkaTemplate.send("order-events", "ORDER_CREATED:" + order.getId());
        return order;
    }

    public void cancelOrder(Long orderId) {
        repository.findById(orderId).ifPresent(order -> order.setStatus("CANCELLED"));
    }

    @Transactional
    public void confirmOrder(Long orderId) {
        repository.findById(orderId).ifPresent(order -> {
            order.setStatus("CONFIRMED");
            repository.save(order); // persist the change
        });
    }


    public Order getOrder(Long orderId) {
        Optional<Order> order =  repository.findById(orderId);
        return order.orElse(null);
    }
}

