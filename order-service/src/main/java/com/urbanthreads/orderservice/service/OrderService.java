package com.urbanthreads.orderservice.service;

import com.urbanthreads.orderservice.DTO.CustomerOrderDTO;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {



    Optional<Map<Long, Integer>> stockQuantity(Map<Long, Integer> itemsCountRequested);
    Optional<UUID> makeOrder(CustomerOrderDTO customerOrderDTO);
}
