package com.urbanthreads.orderservice.controller;

import com.urbanthreads.orderservice.DTO.CustomerOrderDTO;
import com.urbanthreads.orderservice.DTO.ItemDTO;
import com.urbanthreads.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/urban-threads")
public class OrderController {


    @Autowired
    OrderService orderService;
    @PostMapping("/order")
    public ResponseEntity<?> order(@RequestBody CustomerOrderDTO order) {

        List<ItemDTO> itemsWithSmallerQuantity = orderService.stockQuantity(order.getItems());
        String orderId = "Place Holder For Now";

        if (itemsWithSmallerQuantity.size() == 0) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(orderId);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(itemsWithSmallerQuantity);
        }
    }

}
