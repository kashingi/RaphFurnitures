package com.raph_furniture.services;

import com.raph_furniture.dto.ProductDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<String> addProduct(ProductDto dto);
    ResponseEntity<List<ProductDto>> getAllProducts();
    ResponseEntity<ProductDto> getProduct(Long id);
    ResponseEntity<String> updateProduct(Long id, ProductDto dto);
    ResponseEntity<String> updateProductStatus(Long id, boolean active);
    ResponseEntity<String> updateStock(Long id, int stock);
    ResponseEntity<String> deleteProduct(Long id);
}
