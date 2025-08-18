package com.raph_furniture.services;

import com.raph_furniture.dto.ProductImageDto;
import java.util.List;

public interface ProductImageService {
    ProductImageDto create(ProductImageDto dto);
    List<ProductImageDto> findAll();
    ProductImageDto findOne(Long id);
    List<ProductImageDto> findByProduct(Long productId);
    ProductImageDto update(Long id, ProductImageDto dto);
    void updateStatus(Long id, boolean active);
    void delete(Long id);
}
