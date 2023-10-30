package com.urbanthreads.orderservice.DTO;

import com.urbanthreads.orderservice.model.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class CustomerOrderDTO {

     Map<Long, Integer> itemsCountRequested;
     String street;

     String city;

     String state;

     String postalCode;

     String country;

     String propertyType;

     String firstName;

     String lastName;

     String email;

     BigDecimal unitPrice;

     BigDecimal totalPrice;

     String ccv;

     String creditCardCompany;

     String expirationDate;

     String cardHolderName;

     String cardNumber;

     String orderStatus;

     String purchaseDate;

     BigDecimal purchaseAmount;

}
