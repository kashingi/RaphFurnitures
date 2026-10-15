package com.raph_furniture.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.time.Instant;

//Add your annotations here
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String name;

    private String description;

    private Double price;

    private Boolean status;

    private String productImage;

    private Double stock;

    private Double rating;

    private Long categoryId;
}
