package com.raph_furniture.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//Add your annotations here
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor

@NamedQuery( name = "Cart.getAllCart", query = "SELECT new com.raph_furniture.wrapper.CartWrapper(c.id, c.user.id, c.user.name, c.user.email, " +
            "c.product.id, c.product.name, c.product.description, c.product.price, c.quantity) " +
            "FROM Cart c WHERE c.user.id = :userId")

public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;


}
