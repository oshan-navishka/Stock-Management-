package com.example.Task_III.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    private String productName;
    private String description;
    private double price;
    private int quantity;
    private Long categoryId;
    private Long supplierId;
    private String categoryName;
    private String supplierName;
}
