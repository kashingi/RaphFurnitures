package com.raph_furniture.dto;

import lombok.Data;

//Add your annotations here
@Data
public class OrderDto {

    private Long cartId;

    private String paymentMethod;

    private Integer quantity;
}
