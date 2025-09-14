package com.raph_furniture.services;

import com.raph_furniture.dto.CartDao;
import com.raph_furniture.wrapper.CartWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;

//Add your imports here
public interface CartService {
    ResponseEntity<String> addToCart(CartDao cartDao);

    ResponseEntity<List<CartWrapper>> getCart();

    ResponseEntity<String> updateCart(Long id, CartDao cartDao);

    ResponseEntity<String> removeFromCart(Long id);
}
