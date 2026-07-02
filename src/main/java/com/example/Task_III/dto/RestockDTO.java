package com.example.Task_III.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestockDTO {
    private Long restockId;
    private int quantity;
    private LocalDate restockDate;
    private Long productId;
    private Long supplierId;
    private String productName;
    private String supplierName;
}
