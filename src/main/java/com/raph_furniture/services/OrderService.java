package com.raph_furniture.services;

import com.raph_furniture.dto.CartDao;
import com.raph_furniture.dto.OrderDto;
import com.raph_furniture.dto.OrderStatusDto;
import com.raph_furniture.wrapper.OrderWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;

//Add your annotations here
public interface OrderService {
    ResponseEntity<String> placeOrder(OrderDto orderDto);

    ResponseEntity<List<OrderWrapper>> getOrders();


    ResponseEntity<String> updateOrderStatus(Long id, OrderStatusDto statusDto);
}
