package com.urbanthreads.orderservice.service;

import com.urbanthreads.orderservice.DTO.CustomerOrderDTO;
import com.urbanthreads.orderservice.DTO.PaymentDTO;
import com.urbanthreads.orderservice.model.OrderItem;
import com.urbanthreads.orderservice.model.OrderStatus;
import com.urbanthreads.orderservice.model.Payment;
import com.urbanthreads.orderservice.model.Purchase;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.math.BigDecimal;
import java.net.URI;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Optional<Map<Long, Integer>> stockQuantity(Map<Long, Integer> itemsCountRequested) {
        List<Long> ids = new ArrayList<>(itemsCountRequested.keySet());
        String url = "http://localhost:8080/urban-threads/availableitems";

        // Build the URI with query parameters
        URI targetUrl = UriComponentsBuilder.fromUriString(url)
                .queryParam("requestedItems", ids)
                .build()
                .encode()
                .toUri();

        // Define the response type reference for the REST call
        ParameterizedTypeReference<Map<Long, Integer>> responseTypeRef = new ParameterizedTypeReference<>() {
        };

        // Use exchange to make the call and receive a typed response
        ResponseEntity<Map<Long, Integer>> responseEntity = restTemplate.exchange(
                targetUrl,
                HttpMethod.GET,
                null,
                responseTypeRef
        );

        Map<Long, Integer> itemsAvailable = responseEntity.getBody();


        Map<Long, Integer> itemsNotAvailable = new HashMap<>();
        // Iterate over the items requested and compare with the available items
        itemsCountRequested.forEach((requestedId, requestedQuantity) -> {
            Integer itemInStock = itemsAvailable.get(requestedId);
            if (itemInStock == null || requestedQuantity > itemInStock) {
                itemsNotAvailable.put(requestedId, itemInStock != null ? itemInStock : 0);
            }
        });

        return Optional.of(itemsNotAvailable);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public Optional<UUID> makeOrder(CustomerOrderDTO customerOrderDTO) {
        /*
        1. Call reduce stock. If return is integer != order amount, return message. Else, return id.
        2. In front end, if message is returned, front end should call "/orderstock" and display exact stock issue.
         */

        //IF STOCK IS ENOUGH
        Map<Long, Integer> itemsCountRequested = customerOrderDTO.getItemsCountRequested();
        Purchase purchase = new Purchase();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> entry : itemsCountRequested.entrySet()) {
            Long itemId = entry.getKey();
            Integer quantity = entry.getValue();

            OrderItem orderItem = new OrderItem();
            orderItem.setItemId(itemId);
            orderItem.setQuantity(quantity);

            BigDecimal unitPrice = getUnitPriceForItem(itemId);
            orderItem.setUnitPrice(unitPrice);

            //purchase.addOrderItem(orderItem);

            totalAmount = totalAmount.add(orderItem.getTotalPrice());
        }

        purchase.setPurchaseAmount(totalAmount);

        //TODO: save purchase to database
        //purchaseRepository.save(purchase);

        //set payment information from the front end
        //TODO: refactor this part to a separate method
        Payment payment = new Payment();
        payment.setCardHolderName(customerOrderDTO.getCardHolderName());
        payment.setCardNumber(customerOrderDTO.getCardNumber());
        payment.setExpirationDate(customerOrderDTO.getExpirationDate());
        payment.setCcv(customerOrderDTO.getCcv());
        payment.setPurchase(purchase);


        PaymentDTO paymentDTO = new PaymentDTO(payment, totalAmount);


        //TODO: call payment service
        boolean paymentSuccess = callBankingServiceToMakePayment(paymentDTO);
        if (paymentSuccess) {
            purchase.setOrderStatus(OrderStatus.PAID);
            return Optional.ofNullable(purchase.getId());
        } else {
            // TODO: handle payment failure
            return Optional.empty();
        }
    }


    private boolean callBankingServiceToMakePayment(PaymentDTO paymentDTO) {
        //TODO: replace with banking service url
        String url = "http://banking-service-url/make-payment";

        ResponseEntity<?> response = restTemplate.postForEntity(url, paymentDTO, ResponseEntity.class);

        return response.getStatusCode() == HttpStatus.OK;
    }

    private BigDecimal getUnitPriceForItem(Long itemId) {
        //TODO: get unit price from inventory service
        return BigDecimal.valueOf(10);
    }
}

