package org.avi.shoppingcartmcp.tools;

import org.avi.shoppingcartmcp.entity.CartItem;
import org.avi.shoppingcartmcp.repository.CartItemRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ShoppingCartMCPService {
    private final CartItemRepository cartItemRepository;

    public ShoppingCartMCPService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    // catalog service
    private static final Map<String, Double> PRODUCTS = Map.of(
            "iPhone", 79999.0,
            "MacBook Air", 129999.0,
            "Boat Airdopes", 19999.0
    );

    //tools
    @Tool(name = "addProductToCart",
            description = "Adds a product to the shopping cart")
    public String addProductToCart(@ToolParam(name = "productName", description = "Name of the product to add") String productName, @ToolParam(name = "quantity", description = "Quantity of the product to add") int quantity) {
        if(!PRODUCTS.containsKey(productName)) {
            return "Product not found";
        }
        Double price = PRODUCTS.get(productName);
        CartItem cartItem = cartItemRepository.findByProductId(productName);
        if (cartItem == null) {
            cartItem = new CartItem(null, productName, productName, price, quantity);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setPrice(price * quantity);
        cartItemRepository.save(cartItem);
        return "%d %s added to cart".formatted(quantity, productName);
    }

    @Tool(name = "removeProductFromCart",
            description = "Removes a product from the shopping cart")
    public String removeProductFromCart(@ToolParam(name = "productName", description = "Name of the product to remove") String productName) {
        CartItem cartItem = cartItemRepository.findByProductId(productName);
        if (cartItem == null) {
            return "Product not found";
        }
        cartItemRepository.deleteByProductId(productName);
        return "%s removed from cart".formatted(productName);
    }

    @Tool(name = "getCart",
            description = "Returns the shopping cart")
    public String getCart() {
        return cartItemRepository.findAll().stream().map(CartItem::toString).reduce("", (a, b) -> a + "\n" + b);
    }
    private String getCartByProduct(String productName) {
        CartItem cartItem = cartItemRepository.findByProductId(productName);
        return cartItem == null ? "Product not found" : cartItem.toString();
    }

    @Tool(name = "getCartTotal",
            description = "Returns the total price of the shopping cart")
    public double getCartTotal() {
        return cartItemRepository.findAll().stream().mapToDouble(CartItem::getPrice).sum();
    }

    @Tool(name = "getCartItems",
            description = "Returns the shopping cart items")
    public List<CartItem> getCartItems() {
        return cartItemRepository.findAll();
    }
}
```