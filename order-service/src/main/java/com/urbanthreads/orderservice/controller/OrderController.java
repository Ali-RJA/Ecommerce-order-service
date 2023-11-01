package com.urbanthreads.orderservice.controller;

import com.urbanthreads.orderservice.DTO.CustomerOrderDTO;
import com.urbanthreads.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/urban-threads")
public class OrderController {


    @Autowired
    OrderService orderService;
    @PostMapping("/order")
    public ResponseEntity<?> order(@RequestBody CustomerOrderDTO order) {
        Optional<Map<Long, Integer>> itemsUnavailable = orderService.stockQuantity(order.getItemsCountRequested());
        if (itemsUnavailable.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(itemsUnavailable.get());

        }
        Optional<UUID> orderId = orderService.makeOrder(order);
         if (orderId.isPresent()) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(orderId);
        }
        else {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something wrong with order DTO passed");

         }
    }

    @PostMapping("/orderstock")
    public ResponseEntity<?> order(@RequestBody Map<Long, Integer> orderStock) {
        Optional<Map<Long, Integer>> itemsUnavailable = orderService.stockQuantity(orderStock);
        if (itemsUnavailable.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(itemsUnavailable.get());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("All items available!");
    }

    }
