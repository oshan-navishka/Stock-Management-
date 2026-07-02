package com.example.Task_III.service;

import com.example.Task_III.dto.SupplierDTO;

import java.util.List;

public interface SupplierService {
    void saveSupplier(SupplierDTO supplierDTO);
    void updateSupplier(SupplierDTO supplierDTO);
    List<SupplierDTO> getAllSuppliers();
}
