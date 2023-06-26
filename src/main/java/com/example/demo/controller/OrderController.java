package com.example.demo.controller;


import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.services.OrderService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/")
    public List<Order> getAllOrders(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate)
    {
        return orderService.getAllOrders(startDate, endDate);
    };

    @GetMapping("/{id}")
    public Order getOrderById(@RequestParam Long id){
        return orderService.getOrderById(id);
    }

    @PostMapping("/")
    public Order postOrder(@RequestBody List<OrderItem.Projection> projections){
        return orderService.createOrderFromItems(projections);
    }

    @PatchMapping("/{id}/status")
    public void putOrderStatus(@RequestParam Long id){
        orderService.changeOrderStatus(id);
    }
}
