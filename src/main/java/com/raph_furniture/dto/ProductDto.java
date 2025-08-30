package com.raph_furniture.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Add your annotations here
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String name;

    private String description;

    private Double price;

    private String status;

    private Long categoryId;

//    public ProductDto(String name, String description, Double price, String status, Long categoryId) {
//        this.name = name;
//        this.description = description;
//        this.price = price;
//        this.status = status;
//        this.categoryId = categoryId;
//    }

}
