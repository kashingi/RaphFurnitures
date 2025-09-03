package com.raph_furniture.repository;

import com.raph_furniture.model.Product;
import com.raph_furniture.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//Add your annotations here
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);


    List<ProductWrapper> getAllProduct();

    List<ProductWrapper> getProductByCategory(Long id);

}
