package com.urbanthreads.orderservice.DTO;

import com.urbanthreads.orderservice.model.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class CustomerOrderDTO {

     String street;

     String city;

     String state;

     String postalCode;

     String country;

     String propertyType;

     String firstName;

     String lastName;

     String email;

     Address address;

     int quantity;

     BigDecimal unitPrice;

     BigDecimal totalPrice;

     String ccv;

     String creditCardCompany;

     String expirationDate;

     String cardHolderName;

     String cardNumber;

     Purchase purchase;

     String orderStatus;

     LocalDateTime purchaseDate;

     BigDecimal purchaseAmount;

}
