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

    //Update product
    @PutMapping(path = "/updateProduct/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }

    //get product by category
    @GetMapping(path = "/getProductByCategory/{id}")
    public ResponseEntity<List<ProductWrapper>> getProductByCategory(@PathVariable Long id) {
        return productService.getProductByCategory(id);
    }


    //Update product status
    @PutMapping(path = "/updateProductStatus/{id}")
    public ResponseEntity<String> updateProductStatus(@PathVariable Long id, @RequestBody ProductDto productDto) {
        return productService.updateProductStatus(id, productDto);
    }

    //delete product
    @DeleteMapping(path = "/deleteProduct/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }
}
