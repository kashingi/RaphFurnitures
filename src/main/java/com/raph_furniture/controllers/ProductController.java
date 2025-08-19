package com.raph_furniture.controllers;

import com.raph_furniture.dto.ProductDto;
import com.raph_furniture.services.ProductService;
import com.raph_furniture.wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Add your annotation here
@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/product")
public class ProductController {

    //Autowire you resources here
    @Autowired
    ProductService productService;

    //add product
    @PostMapping(path = "/addProduct")
    public ResponseEntity<String> addProduct(@RequestBody ProductDto productDto) {
        return productService.addProduct(productDto);
    }

    //get all products
    @GetMapping(path = "/getAllProducts")
    public ResponseEntity<List<ProductWrapper>> getAllProducts() {
        return productService.getAllProducts();
    }
}
