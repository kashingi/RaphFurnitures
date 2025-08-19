package com.raph_furniture.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWrapper {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String status;
    private Long categoryId;
    private String categoryName;

    // Custom lightweight constructors
    public ProductWrapper(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ProductWrapper(Long id, String name, String description, Double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
