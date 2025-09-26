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

    private String productName;

    private String productDescription;

    private Double productPrice;

    private Integer quantity;


}
