package com.raph_furniture.services;


import com.raph_furniture.dto.ProductDto;
import com.raph_furniture.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;

//Add your annotations here
public interface ProductService {
    ResponseEntity<String> addProduct(ProductDto productDto);

    ResponseEntity<List<ProductWrapper>> getAllProducts();
}
