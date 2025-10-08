package com.raph_furniture.dto;

import lombok.Data;

//Add your annotations here
@Data
public class PaymentDto {

    private Long orderId;

    private String phoneNumber;
}
