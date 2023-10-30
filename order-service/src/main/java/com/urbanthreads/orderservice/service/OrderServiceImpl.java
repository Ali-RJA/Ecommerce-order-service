package com.urbanthreads.orderservice.service;

import com.urbanthreads.orderservice.DTO.CustomerOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Optional<Map<Long, Integer>> stockQuantity(Map<Long, Integer> itemsCountRequested) {

        Map<Long, Integer> itemsAvailable = restTemplate.getForObject(
                "http://localhost:8080/urban-threads/availableitems", HashMap.class);
        Map<Long, Integer> itemsNotAvailable = new HashMap<>();
        for (Map.Entry<Long,Integer> itemRequest : itemsCountRequested.entrySet()) {
            int itemInStock = (int) itemsAvailable.get(itemRequest.getKey());
            if (itemRequest.getValue() > itemInStock) {
                itemsNotAvailable.put(itemRequest.getKey(), itemInStock);
            }

        }


        return Optional.of(itemsNotAvailable);
    }

    @Override
    public Optional<UUID> makeOrder(CustomerOrderDTO customerOrderDTO) {




        return null;
    }

}
