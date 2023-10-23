package com.urbanthreads.orderservice.service;

import com.urbanthreads.orderservice.DTO.ItemDTO;

import java.util.List;

public interface OrderService {


    public List<ItemDTO> stockQuantity(List<ItemDTO> ids);


    int stockQuantity(long id);
}
