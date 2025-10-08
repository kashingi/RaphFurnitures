package com.raph_furniture.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

//Add your annotations here
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private  Order order;

    private String phoneNumber;

    private Double amount;

    private String status;//PENDING, SUCCESS, FAILED

    private String checkoutRequestId;

    private String merchantRequestId;

    private String mpesaReceiptNumber;

    private LocalDateTime transactionDate = LocalDateTime.now();
}
