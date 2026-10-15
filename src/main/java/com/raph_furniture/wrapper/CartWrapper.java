package com.raph_furniture.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Add your annotations here
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartWrapper {

    private Long id;

    private String userName;

    private String userEmail;

    private Long productId;

    private String name;

    private String description;

    private Double price;

    private Integer quantity;

    private String productImage;


}
