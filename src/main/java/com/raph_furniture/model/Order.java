package com.raph_furniture.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

//Add your annotations here
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "orders")

@NamedQuery(name = "Order.gerAllOder", query = "SELECT new com.raph_furniture.wrapper.OrderWrapper(o.id, o.user.id, o.user.name, o.user.email, " +
        "o.product.id, o.product.name, o.product.description, o.product.price, o.quantity, o.totalAmount, " +
        "o.paymentMethod, o.paymentStatus, o.orderStatus, o.orderDate) " +
        "FROM Order o WHERE o.user.id = :userId")

public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

}
