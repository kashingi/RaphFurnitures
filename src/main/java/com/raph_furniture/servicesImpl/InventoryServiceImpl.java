package com.raph_furniture.servicesImpl;

import com.raph_furniture.constants.FurnitureConstants;
import com.raph_furniture.dto.InventoryDto;
import com.raph_furniture.jwt.JwtFilter;
import com.raph_furniture.model.Inventory;
import com.raph_furniture.repository.InventoryRepository;
import com.raph_furniture.services.InventoryService;
import com.raph_furniture.utils.FurnitureUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    InventoryRepository repo;

    private boolean valid(InventoryDto d) {
        return d != null &&
                d.getProductId() != null &&
                d.getLocation() != null &&
                d.getQuantity() != null &&
                d.getQuantity() >= 0;
    }

    private Inventory toEntity(InventoryDto d) {
        Inventory e = new Inventory();
        e.setId(d.getId());
        e.setProductId(d.getProductId());
        e.setLocation(d.getLocation());
        e.setQuantity(d.getQuantity());
        return e;
    }

    private InventoryDto toDto(Inventory e) {
        InventoryDto d = new InventoryDto();
        d.setId(e.getId());
        d.setProductId(e.getProductId());
        d.setLocation(e.getLocation());
        d.setQuantity(e.getQuantity());
        return d;
    }

    @Override
    public ResponseEntity<String> addInventory(InventoryDto dto) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            if (!valid(dto)) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

            // Upsert by (productId, location)
            Optional<Inventory> existing = repo.findByProductIdAndLocation(dto.getProductId(), dto.getLocation());
            Inventory e = existing.orElseGet(() -> toEntity(dto));
            e.setQuantity(dto.getQuantity());
            repo.save(e);

            return FurnitureUtils.getResponseEntity("Inventory row added/updated successfully.", HttpStatus.CREATED);
        } catch (DataIntegrityViolationException ex) {
            return FurnitureUtils.getResponseEntity("Duplicate inventory for product/location.", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<InventoryDto>> getAllInventories() {
        try {
            List<InventoryDto> list = repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<InventoryDto> getInventory(Long id) {
        try {
            Optional<Inventory> opt = repo.findById(id);
            if (opt.isPresent()) {
                return new ResponseEntity<>(toDto(opt.get()), HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<InventoryDto>> getInventoriesByProduct(Long productId) {
        try {
            List<InventoryDto> list = repo.findByProductId(productId).stream().map(this::toDto).collect(Collectors.toList());
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateInventory(Long id, InventoryDto dto) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            Optional<Inventory> opt = repo.findById(id);
            if (opt.isEmpty()) {
                return FurnitureUtils.getResponseEntity("Inventory id does not exist.", HttpStatus.NOT_FOUND);
            }
            Inventory e = opt.get();
            if (dto.getProductId() != null) e.setProductId(dto.getProductId());
            if (dto.getLocation() != null) e.setLocation(dto.getLocation());
            if (dto.getQuantity() != null) {
                if (dto.getQuantity() < 0) return FurnitureUtils.getResponseEntity("quantity cannot be negative.", HttpStatus.BAD_REQUEST);
                e.setQuantity(dto.getQuantity());
            }
            repo.save(e);
            return FurnitureUtils.getResponseEntity("Inventory updated successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> setQuantity(Long id, int qty) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            if (qty < 0) return FurnitureUtils.getResponseEntity("quantity cannot be negative.", HttpStatus.BAD_REQUEST);
            Optional<Inventory> opt = repo.findById(id);
            if (opt.isEmpty()) {
                return FurnitureUtils.getResponseEntity("Inventory id does not exist.", HttpStatus.NOT_FOUND);
            }
            Inventory e = opt.get();
            e.setQuantity(qty);
            repo.save(e);
            return FurnitureUtils.getResponseEntity("Quantity set successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> adjustQuantity(Long id, int delta) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            Optional<Inventory> opt = repo.findById(id);
            if (opt.isEmpty()) {
                return FurnitureUtils.getResponseEntity("Inventory id does not exist.", HttpStatus.NOT_FOUND);
            }
            Inventory e = opt.get();
            int next = e.getQuantity() + delta;
            if (next < 0) return FurnitureUtils.getResponseEntity("resulting quantity cannot be negative.", HttpStatus.BAD_REQUEST);
            e.setQuantity(next);
            repo.save(e);
            return FurnitureUtils.getResponseEntity("Quantity adjusted successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteInventory(Long id) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            Optional<Inventory> opt = repo.findById(id);
            if (opt.isEmpty()) {
                return FurnitureUtils.getResponseEntity("Inventory id does not exist.", HttpStatus.NOT_FOUND);
            }
            repo.delete(opt.get());
            return FurnitureUtils.getResponseEntity("Inventory deleted successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
