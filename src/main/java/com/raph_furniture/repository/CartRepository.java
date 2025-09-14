package com.raph_furniture.repository;

import com.raph_furniture.model.Cart;
import com.raph_furniture.wrapper.CartWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//Add your imports here
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query(name = "Cart.getAllCart")
    List<CartWrapper> findByUserId(Long userId);

    boolean existsByUserIdAndProductId(Long userId, Long productId);
}
