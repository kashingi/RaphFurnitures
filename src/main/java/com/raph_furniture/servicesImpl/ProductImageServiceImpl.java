package com.raph_furniture.servicesImpl;

import com.raph_furniture.constants.FurnitureConstants;
import com.raph_furniture.dto.ProductImageDto;
import com.raph_furniture.jwt.JwtFilter;
import com.raph_furniture.model.ProductImage;
import com.raph_furniture.repository.ProductImageRepository;
import com.raph_furniture.services.ProductImageService;
import com.raph_furniture.utils.FurnitureUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    ProductImageRepository repo;

    private boolean valid(ProductImageDto d) {
        return d != null && d.getProductId() != null && d.getUrl() != null;
    }

    private ProductImage toEntity(ProductImageDto d) {
        ProductImage e = new ProductImage();
        e.setId(d.getId());
        e.setProductId(d.getProductId());
        e.setUrl(d.getUrl());
        e.setPrimaryImage(d.getPrimaryImage() != null ? d.getPrimaryImage() : Boolean.FALSE);
        e.setSortOrder(d.getSortOrder());
        e.setAltText(d.getAltText());
        e.setActive(d.getActive() != null ? d.getActive() : Boolean.TRUE);
        return e;
    }

    private ProductImageDto toDto(ProductImage e) {
        ProductImageDto d = new ProductImageDto();
        d.setId(e.getId());
        d.setProductId(e.getProductId());
        d.setUrl(e.getUrl());
        d.setPrimaryImage(e.getPrimaryImage());
        d.setSortOrder(e.getSortOrder());
        d.setAltText(e.getAltText());
        d.setActive(e.getActive());
        return d;
    }

    @Override
    public ResponseEntity<String> addProductImage(ProductImageDto dto) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            if (!valid(dto)) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            repo.save(toEntity(dto));
            return FurnitureUtils.getResponseEntity("Product image added successfully.", HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return FurnitureUtils.getResponseEntity("Invalid image data.", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductImageDto>> getAllProductImages() {
        try {
            List<ProductImageDto> list = repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductImageDto> getProductImage(Long id) {
        try {
            Optional<ProductImage> opt = repo.findById(id);
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
    public ResponseEntity<List<ProductImageDto>> getProductImagesByProduct(Long productId) {
        try {
            List<ProductImageDto> list = repo.findByProductId(productId).stream().map(this::toDto).collect(Collectors.toList());
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProductImage(Long id, ProductImageDto dto) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            Optional<ProductImage> opt = repo.findById(id);
            if (opt.isEmpty()) {
                return FurnitureUtils.getResponseEntity("Product image id does not exist.", HttpStatus.NOT_FOUND);
            }
            ProductImage e = opt.get();
            if (dto.getProductId() != null) e.setProductId(dto.getProductId());
            if (dto.getUrl() != null) e.setUrl(dto.getUrl());
            if (dto.getPrimaryImage() != null) e.setPrimaryImage(dto.getPrimaryImage());
            e.setSortOrder(dto.getSortOrder());
            e.setAltText(dto.getAltText());
            if (dto.getActive() != null) e.setActive(dto.getActive());

            repo.save(e);
            return FurnitureUtils.getResponseEntity("Product image updated successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProductImageStatus(Long id, boolean active) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            Optional<ProductImage> opt = repo.findById(id);
            if (opt.isEmpty()) {
                return FurnitureUtils.getResponseEntity("Product image id does not exist.", HttpStatus.NOT_FOUND);
            }
            ProductImage e = opt.get();
            e.setActive(active);
            repo.save(e);
            return FurnitureUtils.getResponseEntity("Product image status updated successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProductImage(Long id) {
        try {
            if (!JwtFilter.currentUserHasRole("ADMIN")) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            Optional<ProductImage> opt = repo.findById(id);
            if (opt.isEmpty()) {
                return FurnitureUtils.getResponseEntity("Product image id does not exist.", HttpStatus.NOT_FOUND);
            }
            repo.delete(opt.get());
            return FurnitureUtils.getResponseEntity("Product image deleted successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
