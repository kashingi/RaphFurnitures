package com.raph_furniture.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Add your annotations here
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

}
