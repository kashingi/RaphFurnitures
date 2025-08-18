package com.raph_furniture.controllers;

import com.raph_furniture.ApiResponse.ApiResponse;
import com.raph_furniture.dto.InventoryDto;
import com.raph_furniture.jwt.JwtFilter;
import com.raph_furniture.services.InventoryService;
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
@RequestMapping("/api/v1/inventories")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService service;
    private final JwtFilter jwt;

    // Create or upsert inventory for product/location (ADMIN)
    @PostMapping
    public ResponseEntity<ApiResponse<InventoryDto>> create(@Valid @RequestBody InventoryDto dto) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.<InventoryDto>builder().statusCode(401).message("Unauthorized access").data(null).build());
            }
            InventoryDto saved = service.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.<InventoryDto>builder().statusCode(201).message("Inventory created").data(saved).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<InventoryDto>builder().statusCode(400).message(e.getMessage()).data(null).build());
        } catch (Exception e) {
            log.error("create inventory failed dto={}", dto, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<InventoryDto>builder().statusCode(500).message("Something went wrong").data(null).build());
        }
    }

    // Get all inventory rows (PUBLIC)
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<InventoryDto>>> findAll() {
        try {
            List<InventoryDto> list = service.findAll();
            return ResponseEntity.ok(
                    ApiResponse.<List<InventoryDto>>builder().statusCode(200)
                            .message((list == null || list.isEmpty()) ? "No inventory rows found" : "Fetched inventory")
                            .data(list == null ? Collections.emptyList() : list).build());
        } catch (Exception e) {
            log.error("findAll inventory failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<List<InventoryDto>>builder().statusCode(500).message("Failed to fetch inventory")
                            .data(Collections.emptyList()).build());
        }
    }

    // Get inventory rows for a specific product (PUBLIC)
    @GetMapping("/by-product/{productId}")
    public ResponseEntity<ApiResponse<List<InventoryDto>>> byProduct(@PathVariable Long productId) {
        try {
            List<InventoryDto> list = service.findByProduct(productId);
            return ResponseEntity.ok(
                    ApiResponse.<List<InventoryDto>>builder().statusCode(200)
                            .message((list == null || list.isEmpty()) ? "No inventory rows found" : "Fetched inventory")
                            .data(list == null ? Collections.emptyList() : list).build());
        } catch (Exception e) {
            log.error("byProduct inventory failed productId={}", productId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<List<InventoryDto>>builder().statusCode(500).message("Failed to fetch inventory")
                            .data(Collections.emptyList()).build());
        }
    }

    // Get a single inventory row (PUBLIC)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryDto>> findOne(@PathVariable Long id) {
        try {
            InventoryDto dto = service.findOne(id);
            return ResponseEntity.ok(ApiResponse.<InventoryDto>builder().statusCode(200).message("Fetched inventory").data(dto).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<InventoryDto>builder().statusCode(404).message(e.getMessage()).data(null).build());
        } catch (Exception e) {
            log.error("findOne inventory failed id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<InventoryDto>builder().statusCode(500).message("Failed to fetch inventory").data(null).build());
        }
    }

    // Update an inventory row (ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryDto>> update(@PathVariable Long id, @Valid @RequestBody InventoryDto dto) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.<InventoryDto>builder().statusCode(401).message("Unauthorized access").data(null).build());
            }
            InventoryDto updated = service.update(id, dto);
            return ResponseEntity.ok(ApiResponse.<InventoryDto>builder().statusCode(200).message("Inventory updated").data(updated).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<InventoryDto>builder().statusCode(404).message(e.getMessage()).data(null).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<InventoryDto>builder().statusCode(400).message(e.getMessage()).data(null).build());
        } catch (Exception e) {
            log.error("update inventory failed id={}, dto={}", id, dto, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<InventoryDto>builder().statusCode(500).message("Failed to update inventory").data(null).build());
        }
    }

    // Set absolute quantity (ADMIN)
    @PatchMapping("/{id}/set")
    public ResponseEntity<ApiResponse<InventoryDto>> set(@PathVariable Long id, @RequestParam int qty) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.<InventoryDto>builder().statusCode(401).message("Unauthorized access").data(null).build());
            }
            InventoryDto result = service.setQuantity(id, qty);
            return ResponseEntity.ok(ApiResponse.<InventoryDto>builder().statusCode(200).message("Quantity set").data(result).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<InventoryDto>builder().statusCode(400).message(e.getMessage()).data(null).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<InventoryDto>builder().statusCode(404).message(e.getMessage()).data(null).build());
        } catch (Exception e) {
            log.error("set quantity failed id={}, qty={}", id, qty, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<InventoryDto>builder().statusCode(500).message("Failed to set quantity").data(null).build());
        }
    }

    // Increment/decrement quantity (ADMIN) e.g., ?delta=-3
    @PatchMapping("/{id}/adjust")
    public ResponseEntity<ApiResponse<InventoryDto>> adjust(@PathVariable Long id, @RequestParam int delta) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.<InventoryDto>builder().statusCode(401).message("Unauthorized access").data(null).build());
            }
            InventoryDto result = service.adjustQuantity(id, delta);
            return ResponseEntity.ok(ApiResponse.<InventoryDto>builder().statusCode(200).message("Quantity adjusted").data(result).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<InventoryDto>builder().statusCode(400).message(e.getMessage()).data(null).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<InventoryDto>builder().statusCode(404).message(e.getMessage()).data(null).build());
        } catch (Exception e) {
            log.error("adjust quantity failed id={}, delta={}", id, delta, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<InventoryDto>builder().statusCode(500).message("Failed to adjust quantity").data(null).build());
        }
    }

    // Delete an inventory row (ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        ApiResponse.<Void>builder().statusCode(401).message("Unauthorized access").data(null).build());
            }
            service.delete(id);
            return ResponseEntity.ok(ApiResponse.<Void>builder().statusCode(200).message("Inventory deleted").data(null).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<Void>builder().statusCode(404).message(e.getMessage()).data(null).build());
        } catch (Exception e) {
            log.error("delete inventory failed id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder().statusCode(500).message("Failed to delete inventory").data(null).build());
        }
    }
}
