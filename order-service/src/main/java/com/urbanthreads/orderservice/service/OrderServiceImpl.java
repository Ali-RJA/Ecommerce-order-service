package com.urbanthreads.orderservice.service;

import com.urbanthreads.orderservice.DTO.CustomerOrderDTO;
import com.urbanthreads.orderservice.DTO.PaymentDTO;
import com.urbanthreads.orderservice.DTO.ShippingOrderDTO;
import com.urbanthreads.orderservice.model.OrderItem;
import com.urbanthreads.orderservice.model.OrderStatus;
import com.urbanthreads.orderservice.model.Payment;
import com.urbanthreads.orderservice.model.Purchase;
import org.antlr.v4.runtime.misc.LogManager;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import com.urbanthreads.orderservice.repository.PurchaseRepository;
import com.urbanthreads.orderservice.repository.OrderItemRepository;
import com.urbanthreads.orderservice.repository.PaymentRepository;
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

    @Autowired
    PurchaseRepository purchaseRepo;

    @Autowired
    OrderItemRepository orderItemRepo;

    @Autowired
    PaymentRepository paymentRepo;
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
        //if stock is enough
        if(stockQuantity( customerOrderDTO.getItemsCountRequested()).isPresent()) {
            Map<Long, Integer> itemsCountRequested = customerOrderDTO.getItemsCountRequested();
            Map<Long, BigDecimal> unitPrices = customerOrderDTO.getUnitPrice();
            Purchase purchase = new Purchase();
             BigDecimal totalAmount = BigDecimal.ZERO;
            Set<OrderItem> orderItems = new HashSet<>();
            for (Map.Entry<Long, Integer> entry : itemsCountRequested.entrySet()) {
                Long itemId = entry.getKey();
                Integer quantity = entry.getValue();

                OrderItem orderItem = new OrderItem();
                orderItem.setItemId(itemId);
                orderItem.setQuantity(quantity);

                orderItem.setPurchase(purchase);
                BigDecimal unitPrice = unitPrices.get(itemId);
                orderItem.setUnitPrice(unitPrice);
                orderItem.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(quantity)));

                orderItems.add(orderItem);
                //save orderItems to the database
                orderItemRepo.save(orderItem);
                totalAmount = totalAmount.add(orderItem.getTotalPrice());
            }
            purchase.setOrderItems(orderItems);
            purchase.setPurchaseAmount(totalAmount);


            //set payment information from the front end
            Payment payment = new Payment();
            payment.setCardHolderName(customerOrderDTO.getCardHolderName());
            payment.setCardNumber(customerOrderDTO.getCardNumber());
            payment.setExpirationDate(customerOrderDTO.getExpirationDate());
            payment.setCcv(customerOrderDTO.getCcv());
            payment.setPurchase(purchase);
            paymentRepo.save(payment);
            PaymentDTO paymentDTO = new PaymentDTO(payment, totalAmount);

            boolean paymentSuccess = callBankingServiceToMakePayment(paymentDTO);
            if (paymentSuccess) {
                purchase.setOrderStatus(OrderStatus.PAID);
                purchaseRepo.save(purchase);
                return Optional.ofNullable(purchase.getId());
            } else {
                // TODO: handle payment failure
                return Optional.empty();
            }
        }
        else {//if stock is not enough
            return Optional.empty();
        }
    }


    private boolean callBankingServiceToMakePayment(PaymentDTO paymentDTO) {
        String url = "http://localhost:8080/make-payment";
        ResponseEntity<?> response = restTemplate.postForEntity(url, paymentDTO, ResponseEntity.class);
        return response.getStatusCode() == HttpStatus.OK;
    }
    @KafkaListener(topics = "order-shipping", groupId = "us-shipping")
    public void shippingListener(ConsumerRecord<String, ShippingOrderDTO> consumerRecord) {
        System.out.println("Shipping order received: " + consumerRecord.value().getShippingLabel());

        /*
        1. get the order id
        2. call order service to get the Purchase object (which is the order)
        3. set the shipping label
        4. save the purchase object
         */

    }

    public void callShippingCompany(String orderId) {
        ShippingOrderDTO shippingOrderDTO = new ShippingOrderDTO();
        shippingOrderDTO.setOrderId(orderId);
        String url = "http://localhost:8080/urban-threads/shipping/initiate";
        restTemplate.postForObject(url, shippingOrderDTO, ShippingOrderDTO.class);

    }
}

