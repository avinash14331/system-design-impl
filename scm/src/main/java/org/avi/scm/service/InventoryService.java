package org.avi.scm.service;

import lombok.RequiredArgsConstructor;
import org.avi.scm.entity.Inventory;
import org.avi.scm.entity.Order;
import org.avi.scm.repository.InventoryRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void reserveStock(Long orderId, String product, int qty) {
        Inventory inventory = repository.findByProduct(product).orElseThrow();
        if (inventory.getQuantity() >= qty) {
            inventory.setQuantity(inventory.getQuantity() - qty);
            repository.save(inventory);
            kafkaTemplate.send("inventory-events", "STOCK_RESERVED:" + orderId);
        } else {
            kafkaTemplate.send("inventory-events", "STOCK_FAILED:" + orderId);
        }
    }

    public void releaseStock(Long orderId, String product, int qty) {
        Inventory inventory = repository.findByProduct(product).orElseThrow();
        inventory.setQuantity(inventory.getQuantity() + qty);
        repository.save(inventory);
    }

    public Inventory createInventory(String product, int quantity) {
        Inventory inventory = repository.save(new Inventory(null, product, quantity));

        return inventory;
    }
}

