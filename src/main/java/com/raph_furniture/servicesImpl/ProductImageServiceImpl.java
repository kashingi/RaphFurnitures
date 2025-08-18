package com.raph_furniture.servicesImpl;

import com.raph_furniture.dto.ProductImageDto;
import com.raph_furniture.model.ProductImage;
import com.raph_furniture.repository.ProductImageRepository;
import com.raph_furniture.services.ProductImageService;
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
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository repo;

    private ProductImage toEntity(ProductImageDto d) {
        return ProductImage.builder()
                .id(d.getId())
                .productId(d.getProductId())
                .url(d.getUrl())
                .primaryImage(d.getPrimaryImage() != null ? d.getPrimaryImage() : false)
                .sortOrder(d.getSortOrder())
                .altText(d.getAltText())
                .active(d.getActive() != null ? d.getActive() : true)
                .build();
    }

    private ProductImageDto toDto(ProductImage e) {
        return ProductImageDto.builder()
                .id(e.getId())
                .productId(e.getProductId())
                .url(e.getUrl())
                .primaryImage(e.getPrimaryImage())
                .sortOrder(e.getSortOrder())
                .altText(e.getAltText())
                .active(e.getActive())
                .build();
    }

    @Override
    public ProductImageDto create(ProductImageDto dto) {
        try {
            ProductImage saved = repo.save(toEntity(dto));
            return toDto(saved);
        } catch (DataIntegrityViolationException ex) {
            log.error("create image failed: {}", dto, ex);
            throw new IllegalArgumentException("Invalid image data");
        }
    }

    @Override
    public List<ProductImageDto> findAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public ProductImageDto findOne(Long id) {
        return repo.findById(id).map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException("Product image not found"));
    }

    @Override
    public List<ProductImageDto> findByProduct(Long productId) {
        return repo.findByProductId(productId).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public ProductImageDto update(Long id, ProductImageDto dto) {
        ProductImage existing = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product image not found"));

        existing.setProductId(dto.getProductId());
        existing.setUrl(dto.getUrl());
        existing.setPrimaryImage(dto.getPrimaryImage() != null ? dto.getPrimaryImage() : existing.getPrimaryImage());
        existing.setSortOrder(dto.getSortOrder());
        existing.setAltText(dto.getAltText());
        existing.setActive(dto.getActive() != null ? dto.getActive() : existing.getActive());

        return toDto(repo.save(existing));
    }

    @Override
    public void updateStatus(Long id, boolean active) {
        ProductImage img = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Product image not found"));
        img.setActive(active);
        repo.save(img);
    }

    @Override
    public void delete(Long id) {
        ProductImage img = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Product image not found"));
        repo.delete(img);
    }
}
