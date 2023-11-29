package com.urbanthreads.orderservice.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
public class CustomerOrderDTO {

     private Map<Long, Integer> itemsCountRequested;
     private Map<Long, BigDecimal> unitPrice;
     private String street;
     private String city;
     private String state;
     private String postalCode;
     private String country;
     private String firstName;
     private String lastName;
     private String email;

     private String ccv;
     private String creditCardCompany;
     private String expirationDate;
     private String cardHolderName;
     private String cardNumber;

}
