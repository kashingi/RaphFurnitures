package com.raph_furniture.servicesImpl;

import com.raph_furniture.constants.FurnitureConstants;
import com.raph_furniture.dto.ProductDto;
import com.raph_furniture.jwt.JwtFilter;
import com.raph_furniture.model.Category;
import com.raph_furniture.model.Product;
import com.raph_furniture.repository.CategoryRepository;
import com.raph_furniture.repository.ProductRepository;
import com.raph_furniture.services.ProductService;
import com.raph_furniture.utils.FurnitureUtils;
import com.raph_furniture.wrapper.ProductWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//Add your annotations here
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {


    //Autowire your services here
    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<String> addProduct(ProductDto productDto) {
        try {
            if (jwtFilter.isAdmin()) {
                log.info("Inside jwtFilter.isAdmin : ");
                if (validateProductMap(productDto)) {
                    if (!productRepository.existsByName(productDto.getName())) {

                        Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());

                        if (optionalCategory.isPresent()) {
                            Product product = getProduct(productDto, optionalCategory);

                            //save the product
                            productRepository.save(product);

                            return FurnitureUtils.getResponseEntity("Product added successfully.", HttpStatus.CREATED);
                            
                        } else {
                            return FurnitureUtils.getResponseEntity("Category id does not exist.", HttpStatus.OK);
                        }
                    } else {
                        return FurnitureUtils.getResponseEntity("Product already exist.", HttpStatus.OK);
                    }
                }
            } else {
                return FurnitureUtils.getResponseEntity(FurnitureConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return FurnitureUtils.getResponseEntity(FurnitureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProducts() {
        try {
            return new ResponseEntity<>(productRepository.getAllProduct(), HttpStatus.OK);
        }catch (Exception ex) {
            ex.printStackTrace();

            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private boolean validateProductMap(ProductDto productDto) {
        log.info("Inside validate product map : ");
        return productDto.getName() != null &&
                productDto.getPrice() != null &&
                productDto.getDescription() != null &&
                productDto.getStatus() != null &&
                productDto.getCategoryId() != null;
    }

    private static Product getProduct(ProductDto productDto, Optional<Category> optionalCategory) {
        Category category = optionalCategory.get();

        category.setId(productDto.getCategoryId());
        Product product = new Product();

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStatus(productDto.getStatus());
        product.setCategory(category);
        return product;
    }
}
