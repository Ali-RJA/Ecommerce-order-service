package com.urbanthreads.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customer_purchase")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "purchase")
    private Set<OrderItem> orderItems = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @Column(name = "purchase_amount")
    private BigDecimal purchaseAmount;


    @OneToOne
    @JoinColumn(name="payment_id")
    private Payment payment;


}
























