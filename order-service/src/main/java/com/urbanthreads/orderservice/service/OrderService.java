package com.urbanthreads.orderservice.service;

import com.urbanthreads.orderservice.DTO.CustomerOrderDTO;
import com.urbanthreads.orderservice.DTO.ShippingOrderDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {



    Optional<Map<Long, Integer>> stockQuantity(Map<Long, Integer> itemsCountRequested);
    Optional<UUID> makeOrder(CustomerOrderDTO customerOrderDTO);

    void shippingListener(ConsumerRecord<String, ShippingOrderDTO> consumerRecord);
}
