package com.raph_furniture.controllers;

import com.raph_furniture.ApiResponse.ApiResponse;
import com.raph_furniture.dto.ProductImageDto;
import com.raph_furniture.jwt.JwtFilter;
import com.raph_furniture.services.ProductImageService;
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
@RequestMapping("/api/v1/product-images")
@RequiredArgsConstructor
@Slf4j
public class ProductImageController {

    private final ProductImageService service;
    private final JwtFilter jwt;

    // Create image (ADMIN)
    @PostMapping
    public ResponseEntity<ApiResponse<ProductImageDto>> create(@Valid @RequestBody ProductImageDto dto) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.<ProductImageDto>builder().statusCode(401).message("Unauthorized access").data(null).build());
            }
            ProductImageDto saved = service.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.<ProductImageDto>builder().statusCode(201).message("Image created").data(saved).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<ProductImageDto>builder().statusCode(400).message(e.getMessage()).data(null).build());
        } catch (Exception e) {
            log.error("create image failed dto={}", dto, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<ProductImageDto>builder().statusCode(500).message("Something went wrong").data(null).build());
        }
    }

    // Get all images (PUBLIC)
    @GetMapping(path = "/getAll")
    public ResponseEntity<ApiResponse<List<ProductImageDto>>> findAll() {
        try {
            List<ProductImageDto> list = service.findAll();
            return ResponseEntity.ok(
                    ApiResponse.<List<ProductImageDto>>builder().statusCode(200)
                            .message((list == null || list.isEmpty()) ? "No images found" : "Fetched images")
                            .data(list == null ? Collections.emptyList() : list).build());
        } catch (Exception e) {
            log.error("findAll images failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<List<ProductImageDto>>builder().statusCode(500).message("Failed to fetch images")
                            .data(Collections.emptyList()).build());
        }
    }

    // Get images by productId (PUBLIC)
    @GetMapping("/by-product/{productId}")
    public ResponseEntity<ApiResponse<List<ProductImageDto>>> byProduct(@PathVariable Long productId) {
        try {
            List<ProductImageDto> list = service.findByProduct(productId);
            return ResponseEntity.ok(
                    ApiResponse.<List<ProductImageDto>>builder().statusCode(200)
                            .message((list == null || list.isEmpty()) ? "No images found" : "Fetched images")
                            .data(list == null ? Collections.emptyList() : list).build());
        } catch (Exception e) {
            log.error("byProduct images failed productId={}", productId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<List<ProductImageDto>>builder().statusCode(500).message("Failed to fetch images")
                            .data(Collections.emptyList()).build());
        }
    }

    // Get single image (PUBLIC)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductImageDto>> findOne(@PathVariable Long id) {
        try {
            ProductImageDto dto = service.findOne(id);
            return ResponseEntity.ok(ApiResponse.<ProductImageDto>builder()
                    .statusCode(200).message("Fetched image").data(dto).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<ProductImageDto>builder().statusCode(404).message(e.getMessage()).data(null).build());
        } catch (Exception e) {
            log.error("findOne image failed id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<ProductImageDto>builder().statusCode(500).message("Failed to fetch image").data(null).build());
        }
    }

    // Update image (ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductImageDto>> update(@PathVariable Long id, @Valid @RequestBody ProductImageDto dto) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.<ProductImageDto>builder().statusCode(401).message("Unauthorized access").data(null).build());
            }
            ProductImageDto updated = service.update(id, dto);
            return ResponseEntity.ok(ApiResponse.<ProductImageDto>builder().statusCode(200).message("Image updated").data(updated).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<ProductImageDto>builder().statusCode(404).message(e.getMessage()).data(null).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<ProductImageDto>builder().statusCode(400).message(e.getMessage()).data(null).build());
        } catch (Exception e) {
            log.error("update image failed id={}, dto={}", id, dto, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<ProductImageDto>builder().statusCode(500).message("Failed to update image").data(null).build());
        }
    }

    // Toggle image active status (ADMIN)
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(@PathVariable Long id, @RequestParam boolean active) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.<Void>builder().statusCode(401).message("Unauthorized access").data(null).build());
            }
            service.updateStatus(id, active);
            return ResponseEntity.ok(ApiResponse.<Void>builder().statusCode(200).message("Status updated").data(null).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<Void>builder().statusCode(404).message(e.getMessage()).data(null).build());
        } catch (Exception e) {
            log.error("updateStatus image failed id={}, active={}", id, active, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder().statusCode(500).message("Failed to update status").data(null).build());
        }
    }

    // Delete image (ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.<Void>builder().statusCode(401).message("Unauthorized access").data(null).build());
            }
            service.delete(id);
            return ResponseEntity.ok(ApiResponse.<Void>builder().statusCode(200).message("Image deleted").data(null).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<Void>builder().statusCode(404).message(e.getMessage()).data(null).build());
        } catch (Exception e) {
            log.error("delete image failed id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder().statusCode(500).message("Failed to delete image").data(null).build());
        }
    }
}
