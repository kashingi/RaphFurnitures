package com.raph_furniture.servicesImpl;

import com.raph_furniture.constants.FurnitureConstants;
import com.raph_furniture.dto.CartDto;
import com.raph_furniture.jwt.JwtFilter;
import com.raph_furniture.model.Cart;
import com.raph_furniture.model.Product;
import com.raph_furniture.repository.CartRepository;
import com.raph_furniture.repository.ProductRepository;
import com.raph_furniture.services.CartService;
import com.raph_furniture.utils.FurnitureUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired JwtFilter jwtFilter;
    @Autowired CartRepository cartRepo;
    @Autowired ProductRepository productRepo;

    private String currentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || "anonymousUser".equalsIgnoreCase(auth.getName())) {
            return null;
        }
        return auth.getName();
    }

    private CartDto toDto(Cart e, Map<Long, String> nameMap) {
        return CartDto.builder()
                .id(e.getId())
                .userEmail(e.getUserEmail())
                .productId(e.getProductId())
                .productName(nameMap.get(e.getProductId()))
                .quantity(e.getQuantity())
                .unitPrice(e.getUnitPrice())
                .lineTotal(e.getLineTotal())
                .build();
    }

    @Override
    public ResponseEntity<String> addItem(Long productId, Integer qty) {
        try {
            if (!JwtFilter.currentUserHasRole("USER")) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            if (productId == null || qty == null || qty <= 0) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

            String email = currentUserEmail();
            if (email == null) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

            Optional<Product> opt = productRepo.findById(productId);
            if (opt.isEmpty()) return FurnitureUtils.getResponseEntity("Product not found.", HttpStatus.NOT_FOUND);

            Product p = opt.get();
            if (Boolean.FALSE.equals(p.getActive())) {
                return FurnitureUtils.getResponseEntity("Product is inactive.", HttpStatus.BAD_REQUEST);
            }
            if (p.getStock() != null && qty > p.getStock()) {
                return FurnitureUtils.getResponseEntity("Insufficient stock.", HttpStatus.BAD_REQUEST);
            }

            // Upsert by (user, product)
            Cart cart = cartRepo.findByUserEmailAndProductId(email, productId).orElse(null);
            if (cart == null) {
                BigDecimal unit = p.getPrice();
                BigDecimal line = unit.multiply(BigDecimal.valueOf(qty));
                cart = Cart.builder()
                        .userEmail(email)
                        .productId(productId)
                        .quantity(qty)
                        .unitPrice(unit)
                        .lineTotal(line)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build();
            } else {
                int newQty = cart.getQuantity() + qty;
                if (p.getStock() != null && newQty > p.getStock()) {
                    return FurnitureUtils.getResponseEntity("Insufficient stock.", HttpStatus.BAD_REQUEST);
                }
                cart.setQuantity(newQty);
                cart.setUnitPrice(p.getPrice());
                cart.setLineTotal(p.getPrice().multiply(BigDecimal.valueOf(newQty)));
                cart.setUpdatedAt(Instant.now());
            }
            cartRepo.save(cart);

            return FurnitureUtils.getResponseEntity("Item added to cart.", HttpStatus.CREATED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<CartDto>> getMyCart() {
        try {
            if (!JwtFilter.currentUserHasRole("USER")) {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.UNAUTHORIZED);
            }
            String email = currentUserEmail();
            if (email == null) return new ResponseEntity<>(Collections.emptyList(), HttpStatus.UNAUTHORIZED);

            List<Cart> rows = cartRepo.findByUserEmail(email);
            Set<Long> productIds = rows.stream().map(Cart::getProductId).collect(Collectors.toSet());
            Map<Long, String> nameMap = productRepo.findAllById(productIds).stream()
                    .collect(Collectors.toMap(Product::getId, Product::getName));

            List<CartDto> out = rows.stream().map(r -> toDto(r, nameMap)).collect(Collectors.toList());
            return new ResponseEntity<>(out, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateItem(Long cartItemId, Integer qty) {
        try {
            if (!JwtFilter.currentUserHasRole("USER")) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            if (cartItemId == null || qty == null || qty <= 0) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            String email = currentUserEmail();
            if (email == null) return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);

            Cart row = cartRepo.findByIdAndUserEmail(cartItemId, email).orElse(null);
            if (row == null) return FurnitureUtils.getResponseEntity("Cart item not found.", HttpStatus.NOT_FOUND);

            Product p = productRepo.findById(row.getProductId()).orElse(null);
            if (p == null) return FurnitureUtils.getResponseEntity("Product not found.", HttpStatus.NOT_FOUND);
            if (p.getStock() != null && qty > p.getStock()) {
                return FurnitureUtils.getResponseEntity("Insufficient stock.", HttpStatus.BAD_REQUEST);
            }

            row.setQuantity(qty);
            row.setUnitPrice(p.getPrice());
            row.setLineTotal(p.getPrice().multiply(BigDecimal.valueOf(qty)));
            row.setUpdatedAt(Instant.now());
            cartRepo.save(row);

            return FurnitureUtils.getResponseEntity("Cart item updated.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> removeItem(Long cartItemId) {
        try {
            if (!JwtFilter.currentUserHasRole("USER")) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            String email = currentUserEmail();
            if (email == null) return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);

            Cart row = cartRepo.findByIdAndUserEmail(cartItemId, email).orElse(null);
            if (row == null) return FurnitureUtils.getResponseEntity("Cart item not found.", HttpStatus.NOT_FOUND);

            cartRepo.delete(row);
            return FurnitureUtils.getResponseEntity("Cart item removed.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> clear() {
        try {
            if (!JwtFilter.currentUserHasRole("USER")) {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            String email = currentUserEmail();
            if (email == null) return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);

            cartRepo.deleteByUserEmail(email);
            return FurnitureUtils.getResponseEntity("Cart cleared.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
