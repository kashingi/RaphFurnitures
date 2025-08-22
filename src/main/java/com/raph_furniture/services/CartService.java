package com.raph_furniture.services;

import com.raph_furniture.dto.CartDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartService {
    ResponseEntity<String> addItem(Long productId, Integer qty);
    ResponseEntity<List<CartDto>> getMyCart();
    ResponseEntity<String> updateItem(Long cartItemId, Integer qty);
    ResponseEntity<String> removeItem(Long cartItemId);
    ResponseEntity<String> clear();
}
