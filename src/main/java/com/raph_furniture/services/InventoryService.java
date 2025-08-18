package com.raph_furniture.services;

import com.raph_furniture.dto.InventoryDto;
import java.util.List;

public interface InventoryService {
    InventoryDto create(InventoryDto dto);
    List<InventoryDto> findAll();
    InventoryDto findOne(Long id);
    List<InventoryDto> findByProduct(Long productId);
    InventoryDto update(Long id, InventoryDto dto);
    InventoryDto setQuantity(Long id, int qty);
    InventoryDto adjustQuantity(Long id, int delta);
    void delete(Long id);
}
