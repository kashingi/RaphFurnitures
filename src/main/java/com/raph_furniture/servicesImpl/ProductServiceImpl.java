package com.raph_furniture.servicesImpl;

import com.raph_furniture.dto.ProductDto;
import com.raph_furniture.model.Product;
import com.raph_furniture.repository.ProductRepository;
import com.raph_furniture.services.ProductService;
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
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    // ---- mapping helpers ----
    private Product toEntity(ProductDto d) {
        return Product.builder()
                .id(d.getId())
                .name(d.getName())
                .sku(d.getSku())
                .description(d.getDescription())
                .price(d.getPrice())
                .stock(d.getStock())
                .categoryId(d.getCategoryId())
                .active(d.getActive() != null ? d.getActive() : true)
                .build();
    }

    private ProductDto toDto(Product p) {
        return ProductDto.builder()
                .id(p.getId())
                .name(p.getName())
                .sku(p.getSku())
                .description(p.getDescription())
                .price(p.getPrice())
                .stock(p.getStock())
                .categoryId(p.getCategoryId())
                .active(p.getActive())
                .build();
    }

    // ---- service methods ----
    @Override
    public ProductDto create(ProductDto dto) {
        try {
            if (repo.existsBySku(dto.getSku())) {
                throw new IllegalArgumentException("SKU already exists");
            }
            Product p = toEntity(dto);
            p.setId(null);
            return toDto(repo.save(p));
        } catch (DataIntegrityViolationException e) {
            log.error("create product failed: {}", dto, e);
            throw new IllegalArgumentException("Duplicate or invalid product data");
        }
    }

    @Override
    public List<ProductDto> findAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public ProductDto findOne(Long id) {
        return repo.findById(id).map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    @Override
    public ProductDto update(Long id, ProductDto dto) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));

        if (!existing.getSku().equalsIgnoreCase(dto.getSku()) && repo.existsBySku(dto.getSku())) {
            throw new IllegalArgumentException("SKU already exists");
        }

        existing.setName(dto.getName());
        existing.setSku(dto.getSku());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setStock(dto.getStock());
        existing.setCategoryId(dto.getCategoryId());
        existing.setActive(dto.getActive() != null ? dto.getActive() : existing.getActive());

        return toDto(repo.save(existing));
    }

    @Override
    public void updateStatus(Long id, boolean active) {
        Product p = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
        p.setActive(active);
        repo.save(p);
    }

    @Override
    public void updateStock(Long id, int stock) {
        if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative");
        Product p = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
        p.setStock(stock);
        repo.save(p);
    }

    @Override
    public void delete(Long id) {
        Product p = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
        repo.delete(p);
    }
}
