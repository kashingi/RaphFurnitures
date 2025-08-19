package com.raph_furniture.dto;

import lombok.*;
import javax.validation.constraints.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InventoryDto {

    private Long id;

    @NotNull(message = "productId is required")
    private Long productId;

    @NotBlank(message = "location is required")
    private String location;

    @NotNull(message = "quantity is required")
    @Min(value = 0, message = "quantity cannot be negative")
    private Integer quantity;
}
