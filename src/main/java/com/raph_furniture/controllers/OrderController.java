package com.raph_furniture.controllers;

import com.raph_furniture.dto.OrderDto;
import com.raph_furniture.dto.OrderStatusDto;
import com.raph_furniture.services.OrderService;
import com.raph_furniture.wrapper.OrderWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Add your annotations here
@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(path = "/placeOrder")
    public ResponseEntity<String> placeOrder(@RequestBody OrderDto orderDto) {
        return orderService.placeOrder(orderDto);
    }
    //Get orders
    @GetMapping(path = "/getOrders")
    public ResponseEntity<List<OrderWrapper>> getOrders () {
        return orderService.getOrders();
    }

    //Update order
    @PutMapping(path = "/updateOrderStatus/{id}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatusDto statusDto) {
        return orderService.updateOrderStatus(id, statusDto);
    }

}
