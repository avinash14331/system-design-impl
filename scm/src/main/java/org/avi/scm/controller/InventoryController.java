package org.avi.scm.controller;

import lombok.RequiredArgsConstructor;
import org.avi.scm.entity.Inventory;
import org.avi.scm.entity.Order;
import org.avi.scm.repository.InventoryRepository;
import org.avi.scm.service.InventoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;
    @PostMapping
    public Inventory createInventory(@RequestBody Map<String, Object> req) {
        String product = (String) req.get("product");
        int qty = (Integer) req.get("quantity");
        return inventoryService.createInventory(product, qty);
    }
}
