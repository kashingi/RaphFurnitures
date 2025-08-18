package com.raph_furniture.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK to products.id
    @Column(nullable = false)
    private Long productId;

    // absolute or relative URL
    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Boolean primaryImage = false;

    // for gallery ordering
    @Column
    private Integer sortOrder;

    @Column(length = 255)
    private String altText;

    @Column(nullable = false)
    private Boolean active = true;
}
