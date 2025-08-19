package com.raph_furniture.controllers;
import com.raph_furniture.dto.ProductImageDto;
import com.raph_furniture.services.ProductImageService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//Add your annotations here
@RestController
@CrossOrigin
@RequestMapping(path = "/api/v1/product-image")
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;

    @ApiResponse(responseCode = "201", description = "Product image added successfully")
    @PostMapping(path = "/addProductImage")
    public ResponseEntity<String> addProductImage(@RequestBody ProductImageDto dto) {
        return productImageService.addProductImage(dto);
    }

    @GetMapping(path = "/getAllProductImages")
    public ResponseEntity<List<ProductImageDto>> getAllProductImages() {
        return productImageService.getAllProductImages();
    }

    @GetMapping(path = "/getProductImage/{id}")
    public ResponseEntity<ProductImageDto> getProductImage(@PathVariable Long id) {
        return productImageService.getProductImage(id);
    }

    @GetMapping(path = "/getProductImagesByProduct/{productId}")
    public ResponseEntity<List<ProductImageDto>> getProductImagesByProduct(@PathVariable Long productId) {
        return productImageService.getProductImagesByProduct(productId);
    }

    @PutMapping(path = "/updateProductImage/{id}")
    public ResponseEntity<String> updateProductImage(@PathVariable Long id, @RequestBody ProductImageDto dto) {
        return productImageService.updateProductImage(id, dto);
    }

    @PutMapping(path = "/updateProductImageStatus/{id}")
    public ResponseEntity<String> updateProductImageStatus(@PathVariable Long id, @RequestParam boolean active) {
        return productImageService.updateProductImageStatus(id, active);
    }

    @DeleteMapping(path = "/deleteProductImage/{id}")
    public ResponseEntity<String> deleteProductImage(@PathVariable Long id) {
        return productImageService.deleteProductImage(id);
    }
}
