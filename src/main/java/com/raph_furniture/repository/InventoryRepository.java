package com.raph_furniture.repository;

import com.raph_furniture.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductIdAndLocation(Long productId, String location);
    List<Inventory> findByProductId(Long productId);
}
