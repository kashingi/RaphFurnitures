package com.raph_furniture.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//Add your annotations her
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderWrapper {

    private Long id;

    private Long userId;

    private String userName;

    private String userEmail;

    private Long productId;

    private String productName;

    private String productDescription;

    private Double productPrice;

    private Integer quantity;

    private Double totalAmount;

    private String paymentMethod;

    private String paymentStatus;

    private String orderStatus;

    private LocalDateTime orderDate;
}
