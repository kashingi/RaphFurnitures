package com.raph_furniture.services;

import com.raph_furniture.dto.InventoryDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InventoryService {
    ResponseEntity<String> addInventory(InventoryDto dto);
    ResponseEntity<List<InventoryDto>> getAllInventories();
    ResponseEntity<InventoryDto> getInventory(Long id);
    ResponseEntity<List<InventoryDto>> getInventoriesByProduct(Long productId);
    ResponseEntity<String> updateInventory(Long id, InventoryDto dto);
    ResponseEntity<String> setQuantity(Long id, int qty);
    ResponseEntity<String> adjustQuantity(Long id, int delta);
    ResponseEntity<String> deleteInventory(Long id);
}
