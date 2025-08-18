package com.raph_furniture.servicesImpl;

import com.raph_furniture.dto.InventoryDto;
import com.raph_furniture.model.Inventory;
import com.raph_furniture.repository.InventoryRepository;
import com.raph_furniture.services.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repo;

    private Inventory toEntity(InventoryDto d) {
        return Inventory.builder()
                .id(d.getId())
                .productId(d.getProductId())
                .location(d.getLocation())
                .quantity(d.getQuantity())
                .build();
    }

    private InventoryDto toDto(Inventory e) {
        return InventoryDto.builder()
                .id(e.getId())
                .productId(e.getProductId())
                .location(e.getLocation())
                .quantity(e.getQuantity())
                .build();
    }

    @Override
    public InventoryDto create(InventoryDto dto) {
        try {
            // optional: upsert if same (product, location) already exists
            Inventory inv = repo.findByProductIdAndLocation(dto.getProductId(), dto.getLocation())
                    .orElseGet(() -> toEntity(dto));
            inv.setQuantity(dto.getQuantity());
            return toDto(repo.save(inv));
        } catch (DataIntegrityViolationException e) {
            log.error("create inventory failed: {}", dto, e);
            throw new IllegalArgumentException("Duplicate inventory for product/location");
        }
    }

    @Override
    public List<InventoryDto> findAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public InventoryDto findOne(Long id) {
        return repo.findById(id).map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException("Inventory not found"));
    }

    @Override
    public List<InventoryDto> findByProduct(Long productId) {
        return repo.findByProductId(productId).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public InventoryDto update(Long id, InventoryDto dto) {
        Inventory existing = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Inventory not found"));
        existing.setProductId(dto.getProductId());
        existing.setLocation(dto.getLocation());
        if (dto.getQuantity() != null && dto.getQuantity() < 0) {
            throw new IllegalArgumentException("quantity cannot be negative");
        }
        existing.setQuantity(dto.getQuantity());
        return toDto(repo.save(existing));
    }

    @Override
    public InventoryDto setQuantity(Long id, int qty) {
        if (qty < 0) throw new IllegalArgumentException("quantity cannot be negative");
        Inventory inv = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Inventory not found"));
        inv.setQuantity(qty);
        return toDto(repo.save(inv));
    }

    @Override
    public InventoryDto adjustQuantity(Long id, int delta) {
        Inventory inv = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Inventory not found"));
        int next = inv.getQuantity() + delta;
        if (next < 0) throw new IllegalArgumentException("resulting quantity cannot be negative");
        inv.setQuantity(next);
        return toDto(repo.save(inv));
    }

    @Override
    public void delete(Long id) {
        Inventory inv = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Inventory not found"));
        repo.delete(inv);
    }
}
