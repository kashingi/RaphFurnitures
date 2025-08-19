package com.raph_furniture.servicesImpl;

import com.raph_furniture.constants.FurnitureConstants;
import com.raph_furniture.dto.ProductDto;
import com.raph_furniture.jwt.JwtFilter;
import com.raph_furniture.model.Product;
import com.raph_furniture.repository.ProductRepository;
import com.raph_furniture.services.ProductService;
import com.raph_furniture.utils.FurnitureUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    ProductRepository repo;

    private boolean valid(ProductDto d) {
        return d != null &&
                d.getName() != null &&
                d.getSku() != null &&
                d.getPrice() != null &&
                d.getStock() != null &&
                d.getCategoryId() != null;
    }

    private Product toEntity(ProductDto d) {
        Product p = new Product();
        p.setId(d.getId());
        p.setName(d.getName());
        p.setSku(d.getSku());
        p.setDescription(d.getDescription());
        p.setPrice(d.getPrice());
        p.setStock(d.getStock());
        p.setCategoryId(d.getCategoryId());
        p.setActive(d.getActive() != null ? d.getActive() : Boolean.TRUE);
        return p;
    }

    private ProductDto toDto(Product p) {
        ProductDto d = new ProductDto();
        d.setId(p.getId());
        d.setName(p.getName());
        d.setSku(p.getSku());
        d.setDescription(p.getDescription());
        d.setPrice(p.getPrice());
        d.setStock(p.getStock());
        d.setCategoryId(p.getCategoryId());
        d.setActive(p.getActive());
        return d;
    }

    @Override
    public ResponseEntity<String> addProduct(ProductDto dto) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            if (!valid(dto)) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            if (repo.existsBySku(dto.getSku())) {
                return FurnitureUtils.getResponseEntity("SKU already exists.", HttpStatus.BAD_REQUEST);
            }
            Product toSave = toEntity(dto);
            toSave.setId(null);
            repo.save(toSave);
            return FurnitureUtils.getResponseEntity("Product added successfully.", HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return FurnitureUtils.getResponseEntity("Duplicate or invalid product data.", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        try {
            List<ProductDto> list = repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductDto> getProduct(Long id) {
        try {
            Optional<Product> opt = repo.findById(id);
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
    public ResponseEntity<String> updateProduct(Long id, ProductDto dto) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            Optional<Product> opt = repo.findById(id);
            if (opt.isEmpty()) {
                return FurnitureUtils.getResponseEntity("Product id does not exist.", HttpStatus.NOT_FOUND);
            }
            Product existing = opt.get();

            if (dto.getSku() != null && !dto.getSku().equalsIgnoreCase(existing.getSku()) && repo.existsBySku(dto.getSku())) {
                return FurnitureUtils.getResponseEntity("SKU already exists.", HttpStatus.BAD_REQUEST);
            }

            existing.setName(dto.getName() != null ? dto.getName() : existing.getName());
            existing.setSku(dto.getSku() != null ? dto.getSku() : existing.getSku());
            existing.setDescription(dto.getDescription());
            if (dto.getPrice() != null) existing.setPrice(dto.getPrice());
            if (dto.getStock() != null) existing.setStock(dto.getStock());
            if (dto.getCategoryId() != null) existing.setCategoryId(dto.getCategoryId());
            if (dto.getActive() != null) existing.setActive(dto.getActive());

            repo.save(existing);
            return FurnitureUtils.getResponseEntity("Product updated successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProductStatus(Long id, boolean active) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            Optional<Product> opt = repo.findById(id);
            if (opt.isEmpty()) {
                return FurnitureUtils.getResponseEntity("Product id does not exist.", HttpStatus.NOT_FOUND);
            }
            Product p = opt.get();
            p.setActive(active);
            repo.save(p);
            return FurnitureUtils.getResponseEntity("Product status updated successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStock(Long id, int stock) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            if (stock < 0) {
                return FurnitureUtils.getResponseEntity("Stock cannot be negative.", HttpStatus.BAD_REQUEST);
            }
            Optional<Product> opt = repo.findById(id);
            if (opt.isEmpty()) {
                return FurnitureUtils.getResponseEntity("Product id does not exist.", HttpStatus.NOT_FOUND);
            }
            Product p = opt.get();
            p.setStock(stock);
            repo.save(p);
            return FurnitureUtils.getResponseEntity("Product stock updated successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Long id) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            Optional<Product> opt = repo.findById(id);
            if (opt.isEmpty()) {
                return FurnitureUtils.getResponseEntity("Product id does not exist.", HttpStatus.NOT_FOUND);
            }
            repo.delete(opt.get());
            return FurnitureUtils.getResponseEntity("Product deleted successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
