package com.urbanthreads.orderservice.service;

import com.urbanthreads.orderservice.DTO.ItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private RestTemplate restTemplate;
    @Override
    public int stockQuantity(long id) {
        String url = "http://localhost:8080/urban-threads/items/" + id + "/quantity";
        Integer result = restTemplate.getForObject(url, Integer.class);
        int quantity = (result != null) ? result : 0;
        return quantity;
    }

    @Override
    public List<ItemDTO> stockQuantity(List<ItemDTO> items) {
        List<ItemDTO> itemDTOS = new ArrayList<>();
        int quantity;
        for (ItemDTO desiredItem : items) {
            quantity = stockQuantity(desiredItem.getId());
            if (quantity < desiredItem.getQuantity()) {
                itemDTOS.add(new ItemDTO(desiredItem.getId(), quantity));
            }
        }


        return itemDTOS;
    }
}
