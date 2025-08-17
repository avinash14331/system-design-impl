package org.avi.scm.controller;

import lombok.RequiredArgsConstructor;
import org.avi.scm.entity.Order;
import org.avi.scm.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public Order createOrder(@RequestBody Map<String, Object> req) {
        String product = (String) req.get("product");
        int qty = (Integer) req.get("quantity");
        String paymentMode = (String) req.get("paymentMode");
        return orderService.createOrder(product, qty,paymentMode );
    }
}

