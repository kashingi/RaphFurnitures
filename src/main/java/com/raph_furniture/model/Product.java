package com.raph_furniture.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

//Add your annotations here
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
@NamedQuery(name = "Product.getAllProduct", query = "select new com.raph_furniture.wrapper.ProductWrapper(p.id,p.name,p.description,p.price,p.status,p.productImage,p.stock,p.rating,p.category.name) from Product p")

@NamedQuery(name = "Product.getProductByCategory", query = "select new com.raph_furniture.wrapper.ProductWrapper(" +
        "p.id, p.name, p.description, p.price, p.status,p.productImage, p.stock,p.rating, p.category.name) " +
        "from Product p where p.category.id=:id and p.status='true'")

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "status")
    private Boolean status;

    @Lob
    @Column(name = "product_image")
    private String productImage;

    @Column(name = "stock")
    private Double stock;

    @Column(name = "rating")
    private Double rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_fk", nullable = false)
    private Category category;

}
