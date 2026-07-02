package com.example.Task_III.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private LocalDate orderDate;
    private String status;
    private String customerName;
    private List<OrderItemDTO> orderItems;
}
