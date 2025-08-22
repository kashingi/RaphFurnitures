package com.raph_furniture.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {
    private Long id;
    private String userEmail;
    private Long productId;
    private String productName;  // optional: filled from Product table
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}
