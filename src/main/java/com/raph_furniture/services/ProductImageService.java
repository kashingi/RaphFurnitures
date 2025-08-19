package com.raph_furniture.services;

import com.raph_furniture.dto.ProductImageDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductImageService {
    ResponseEntity<String> addProductImage(ProductImageDto dto);
    ResponseEntity<List<ProductImageDto>> getAllProductImages();
    ResponseEntity<ProductImageDto> getProductImage(Long id);
    ResponseEntity<List<ProductImageDto>> getProductImagesByProduct(Long productId);
    ResponseEntity<String> updateProductImage(Long id, ProductImageDto dto);
    ResponseEntity<String> updateProductImageStatus(Long id, boolean active);
    ResponseEntity<String> deleteProductImage(Long id);
}
