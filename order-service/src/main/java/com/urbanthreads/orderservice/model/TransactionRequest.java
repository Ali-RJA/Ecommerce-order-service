package com.urbanthreads.orderservice.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class TransactionRequest {
    @Getter
    @Setter
    private Payment card;
    private BigDecimal totalAmount;
    //TODO: set these two fields, later sent them to the payment service

}

