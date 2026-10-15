package com.raph_furniture.controllers;

import com.raph_furniture.dto.CartDto;
import com.raph_furniture.services.CartService;
import com.raph_furniture.wrapper.CartWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Add your imports here
@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    //add to cart
    @PostMapping(path = "/addToCart")
    public ResponseEntity<String> addToCart(@RequestBody CartDto cartDto) {

        return cartService.addToCart(cartDto);
    }

    //get all cart items
    @GetMapping(path = "/getCart")
    public ResponseEntity<List<CartWrapper>> getCart() {
        return cartService.getCart();
    }

    //Update cart
    @PatchMapping(path = "/updateCart/{id}")
    public ResponseEntity<String> updateCart(@PathVariable Long id, @RequestBody CartDto cartDto) {
        return cartService.updateCart(id, cartDto);
    }

    //Remove cart item
    @DeleteMapping(path = "/removeFromCart/{id}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long id) {
        return cartService.removeFromCart(id);
    }
}
