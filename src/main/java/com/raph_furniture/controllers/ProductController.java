package com.raph_furniture.controllers;

import com.raph_furniture.ApiResponse.ApiResponse;
import com.raph_furniture.dto.ProductDto;
import com.raph_furniture.jwt.JwtFilter;
import com.raph_furniture.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/products") // RESTful plural
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService service;
    private final JwtFilter jwtFilter;

    // CREATE (ADMIN): POST /api/v1/products
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> create(@Valid @RequestBody ProductDto dto) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.<ProductDto>builder().statusCode(401).message("Unauthorized access").data(null).build()
                );
            }
            ProductDto saved = service.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.<ProductDto>builder().statusCode(201).message("Product created").data(saved).build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<ProductDto>builder().statusCode(400).message(e.getMessage()).data(null).build()
            );
        } catch (Exception e) {
            log.error("create product failed dto={}", dto, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<ProductDto>builder().statusCode(500).message("Something went wrong").data(null).build()
            );
        }
    }

    // READ ALL (PUBLIC): GET /api/v1/products
    @GetMapping(path = "/getAllproducts")
    public ResponseEntity<ApiResponse<List<ProductDto>>> findAll() {
        try {
            List<ProductDto> list = service.findAll();
            return ResponseEntity.ok(
                    ApiResponse.<List<ProductDto>>builder()
                            .statusCode(200)
                            .message((list == null || list.isEmpty()) ? "No products found" : "Fetched products")
                            .data(list == null ? Collections.emptyList() : list)
                            .build()
            );
        } catch (Exception e) {
            log.error("findAll products failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<List<ProductDto>>builder()
                            .statusCode(500).message("Failed to fetch products").data(Collections.emptyList()).build()
            );
        }
    }

    // READ ONE (PUBLIC): GET /api/v1/products/{id}
    @GetMapping(path = "getproductby/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> findOne(@PathVariable Long id) {
        try {
            ProductDto dto = service.findOne(id);
            return ResponseEntity.ok(
                    ApiResponse.<ProductDto>builder().statusCode(200).message("Fetched product").data(dto).build()
            );
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<ProductDto>builder().statusCode(404).message(e.getMessage()).data(null).build()
            );
        } catch (Exception e) {
            log.error("findOne product failed id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<ProductDto>builder().statusCode(500).message("Failed to fetch product").data(null).build()
            );
        }
    }

    // UPDATE (ADMIN): PUT /api/v1/products/{id}
    @PutMapping(path = "updateproduct/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> update(@PathVariable Long id, @Valid @RequestBody ProductDto dto) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.<ProductDto>builder().statusCode(401).message("Unauthorized access").data(null).build()
                );
            }
            ProductDto updated = service.update(id, dto);
            return ResponseEntity.ok(
                    ApiResponse.<ProductDto>builder().statusCode(200).message("Product updated").data(updated).build()
            );
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<ProductDto>builder().statusCode(404).message(e.getMessage()).data(null).build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<ProductDto>builder().statusCode(400).message(e.getMessage()).data(null).build()
            );
        } catch (Exception e) {
            log.error("update product failed id={}, dto={}", id, dto, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<ProductDto>builder().statusCode(500).message("Failed to update product").data(null).build()
            );
        }
    }

    // PARTIAL UPDATE - STATUS (ADMIN): PATCH /api/v1/products/{id}/status?active=true|false
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(@PathVariable Long id, @RequestParam boolean active) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.<Void>builder().statusCode(401).message("Unauthorized access").data(null).build()
                );
            }
            service.updateStatus(id, active);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder().statusCode(200).message("Status updated").data(null).build()
            );
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<Void>builder().statusCode(404).message(e.getMessage()).data(null).build()
            );
        } catch (Exception e) {
            log.error("updateStatus failed id={}, active={}", id, active, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder().statusCode(500).message("Failed to update status").data(null).build()
            );
        }
    }

    // PARTIAL UPDATE - STOCK (ADMIN): PATCH /api/v1/products/{id}/stock?stock=10
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<Void>> updateStock(@PathVariable Long id, @RequestParam int stock) {
        try {
            if (!JwtFilter.currentUserHasRole("Admin")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.<Void>builder().statusCode(401).message("Unauthorized access").data(null).build()
                );
            }
            service.updateStock(id, stock);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder().statusCode(200).message("Stock updated").data(null).build()
            );
        } catch (IllegalArgumentException e) {
            HttpStatus code = e.getMessage().toLowerCase().contains("negative") ? HttpStatus.BAD_REQUEST : HttpStatus.NOT_FOUND;
            return ResponseEntity.status(code).body(
                    ApiResponse.<Void>builder().statusCode(code.value()).message(e.getMessage()).data(null).build()
            );
        } catch (Exception e) {
            log.error("updateStock failed id={}, stock={}", id, stock, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder().statusCode(500).message("Failed to update stock").data(null).build()
            );
        }
    }

    // DELETE (ADMIN): DELETE /api/v1/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.<Void>builder().statusCode(401).message("Unauthorized access").data(null).build()
                );
            }
            service.delete(id);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder().statusCode(200).message("Product deleted").data(null).build()
            );
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<Void>builder().statusCode(404).message(e.getMessage()).data(null).build()
            );
        } catch (Exception e) {
            log.error("delete product failed id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder().statusCode(500).message("Failed to delete product").data(null).build()
            );
        }
    }
}
