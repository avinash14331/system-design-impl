package org.avi.scm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    private String product;
    private int quantity;
    private String status; // CREATED, CONFIRMED, CANCELLED
    private String paymentMode; // CREDIT_CARD, PAYPAL, UPI
}

