package com.raph_furniture.dto;

import lombok.*;
import javax.validation.constraints.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductImageDto {

    private Long id;

    @NotNull(message = "productId is required")
    private Long productId;

    @NotBlank(message = "url is required")
    private String url;

    private Boolean primaryImage = false;

    @Min(value = 0, message = "sortOrder cannot be negative")
    private Integer sortOrder;

    @Size(max = 255, message = "altText too long")
    private String altText;

    private Boolean active = true;
}
