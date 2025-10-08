package com.raph_furniture.repository;

import com.raph_furniture.model.Order;
import com.raph_furniture.wrapper.OrderWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//Add your annotations here
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(name = "Order.gerAllOder")
    List<OrderWrapper> findByUserId(Long userId);
}
