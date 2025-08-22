package com.raph_furniture.repository;

import com.raph_furniture.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserEmail(String userEmail);
    Optional<Cart> findByUserEmailAndProductId(String userEmail, Long productId);
    Optional<Cart> findByIdAndUserEmail(Long id, String userEmail);
    void deleteByUserEmail(String userEmail);
}
