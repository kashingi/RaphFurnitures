package com.raph_furniture.controllers;

import com.raph_furniture.dto.CartDto;
import com.raph_furniture.services.CartService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @ApiResponse(responseCode = "201", description = "Item added to cart")
    @PostMapping(path = "/addItem")
    public ResponseEntity<String> addItem(@RequestParam Long productId, @RequestParam Integer qty) {
        return cartService.addItem(productId, qty);
    }

    @GetMapping(path = "/getMyCart")
    public ResponseEntity<List<CartDto>> getMyCart() {
        return cartService.getMyCart();
    }

    @PutMapping(path = "/updateItem/{cartItemId}")
    public ResponseEntity<String> updateItem(@PathVariable Long cartItemId, @RequestParam Integer qty) {
        return cartService.updateItem(cartItemId, qty);
    }

    @DeleteMapping(path = "/removeItem/{cartItemId}")
    public ResponseEntity<String> removeItem(@PathVariable Long cartItemId) {
        return cartService.removeItem(cartItemId);
    }

    @DeleteMapping(path = "/clear")
    public ResponseEntity<String> clear() {
        return cartService.clear();
    }
}
