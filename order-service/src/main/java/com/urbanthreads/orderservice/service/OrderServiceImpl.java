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




        return null;
    }

    @Override
    public Optional<UUID> makeOrder(CustomerOrderDTO customerOrderDTO) {




        return null;
    }

}
