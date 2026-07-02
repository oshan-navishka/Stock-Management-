package com.example.Task_III.service;

import com.example.Task_III.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    void saveOrder(OrderDTO orderDTO);
    void processOrder(Long orderId);
    List<OrderDTO> getRecentOrders();
}
