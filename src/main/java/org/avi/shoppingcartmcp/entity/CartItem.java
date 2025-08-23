package org.avi.shoppingcartmcp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cart_items")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItem {
    @Id
    private String id;
    private String productId;
    private String productName;
    private double price;
    private int quantity;
}
