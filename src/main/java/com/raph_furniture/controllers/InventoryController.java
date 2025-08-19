package com.raph_furniture.controllers;

import com.raph_furniture.dto.InventoryDto;
import com.raph_furniture.services.InventoryService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Add your annotations here
@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @ApiResponse(responseCode = "201", description = "Inventory row added successfully")
    @PostMapping(path = "/addInventory")
    public ResponseEntity<String> addInventory(@RequestBody InventoryDto dto) {
        return inventoryService.addInventory(dto);
    }

    @GetMapping(path = "/getAllInventories")
    public ResponseEntity<List<InventoryDto>> getAllInventories() {
        return inventoryService.getAllInventories();
    }

    @GetMapping(path = "/getInventory/{id}")
    public ResponseEntity<InventoryDto> getInventory(@PathVariable Long id) {
        return inventoryService.getInventory(id);
    }

    @GetMapping(path = "/getInventoriesByProduct/{productId}")
    public ResponseEntity<List<InventoryDto>> getInventoriesByProduct(@PathVariable Long productId) {
        return inventoryService.getInventoriesByProduct(productId);
    }

    @PutMapping(path = "/updateInventory/{id}")
    public ResponseEntity<String> updateInventory(@PathVariable Long id, @RequestBody InventoryDto dto) {
        return inventoryService.updateInventory(id, dto);
    }

    @PutMapping(path = "/setQuantity/{id}")
    public ResponseEntity<String> setQuantity(@PathVariable Long id, @RequestParam int qty) {
        return inventoryService.setQuantity(id, qty);
    }

    @PutMapping(path = "/adjustQuantity/{id}")
    public ResponseEntity<String> adjustQuantity(@PathVariable Long id, @RequestParam int delta) {
        return inventoryService.adjustQuantity(id, delta);
    }

    @DeleteMapping(path = "/deleteInventory/{id}")
    public ResponseEntity<String> deleteInventory(@PathVariable Long id) {
        return inventoryService.deleteInventory(id);
    }
}
