package com.raph_furniture.services;

import com.raph_furniture.dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto create(ProductDto dto);
    List<ProductDto> findAll();
    ProductDto findOne(Long id);
    ProductDto update(Long id, ProductDto dto);
    void updateStatus(Long id, boolean active);
    void updateStock(Long id, int stock);
    void delete(Long id);
}
