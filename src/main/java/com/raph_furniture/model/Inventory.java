package com.raph_furniture.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(
        name = "inventories",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_inventory_product_location",
                columnNames = {"product_id", "location"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK to products.id
    @Column(name = "product_id", nullable = false)
    private Long productId;

    // store/warehouse code
    @Column(length = 64, nullable = false)
    private String location = "DEFAULT";

    // on-hand absolute quantity
    @Column(nullable = false)
    private Integer quantity;
}
