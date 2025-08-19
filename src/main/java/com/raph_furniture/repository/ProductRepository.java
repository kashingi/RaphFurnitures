package com.raph_furniture.repository;

import com.raph_furniture.model.Product;
import com.raph_furniture.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//Add your annotations here
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);

    @Query(name = "Product.getAllProduct")
    List<ProductWrapper> getAllProduct();
}
