package com.urbanthreads.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name = "payment_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="card_holder_name")
    private String cardHolderName;

    @Column(name="card_number")
    private String cardNumber;

    @OneToOne(mappedBy = "payment")
    private Purchase purchase;

    public int setExpirationDate(String expirationDate) {

        Pattern pattern = Pattern.compile("^(0[1-9]|1[0-2])-[0-9]{4}$");
        Matcher matcher = pattern.matcher(expirationDate);
        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();
        int code = 0;
        if (matcher.matches()) {
            if (Integer.parseInt(expirationDate.substring(3)) > currentYear) {
                code = 1;
            }
            else if (Integer.parseInt(expirationDate.substring(3)) == currentYear) {
                if (Integer.parseInt(expirationDate.substring(0,2)) >= currentMonth) {
                    code = 1;
                }
                else { // old month
                    code = -1;

                }
            }
            else { // old year
                code = -2;

            }
        }
        else { // wrong format
            code = -3;

        }
        if (code > 0) {
            this.expirationDate = expirationDate;
        }
        return code;
    }

    @Column(name = "expiration_date")
    private String expirationDate;

    public int setCcv(String ccv) {
        int code = 0;
        if (ccv.length() == 3 && Character.isDigit(ccv.charAt(0))
        && Character.isDigit(ccv.charAt(1)) && Character.isDigit(ccv.charAt(2))) {
            code = 2;
        }
        else {
            code = -4;
        }
        if (code > 0) {
            this.ccv = ccv;
        }
        return code;
    }

    @Column(name = "ccv")
    private String ccv;

    @Enumerated(EnumType.STRING)
    private CreditCardCompany creditCardCompany;






}
