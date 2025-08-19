package com.raph_furniture.controllers;

import com.raph_furniture.dto.ProductDto;
import com.raph_furniture.services.ProductService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Add your annotations here
@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiResponse(responseCode = "201", description = "Product added successfully")
    @PostMapping(path = "/addProduct")
    public ResponseEntity<String> addProduct(@RequestBody ProductDto dto) {
        return productService.addProduct(dto);
    }

    @GetMapping(path = "/getAllProducts")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping(path = "/getProduct/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @PutMapping(path = "/updateProduct/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody ProductDto dto) {
        return productService.updateProduct(id, dto);
    }

    @PutMapping(path = "/updateProductStatus/{id}")
    public ResponseEntity<String> updateProductStatus(@PathVariable Long id, @RequestParam boolean active) {
        return productService.updateProductStatus(id, active);
    }

    @PutMapping(path = "/updateStock/{id}")
    public ResponseEntity<String> updateStock(@PathVariable Long id, @RequestParam int stock) {
        return productService.updateStock(id, stock);
    }

    @DeleteMapping(path = "/deleteProduct/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }
}
