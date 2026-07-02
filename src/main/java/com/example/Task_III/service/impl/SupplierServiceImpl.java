package com.example.Task_III.service.impl;

import com.example.Task_III.dto.SupplierDTO;
import com.example.Task_III.entity.Supplier;
import com.example.Task_III.repository.SupplierRepository;
import com.example.Task_III.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    private SupplierDTO toDTO(Supplier supplier) {
        if (supplier == null) return null;
        return new SupplierDTO(
                supplier.getSupplierId(),
                supplier.getSupplierName(),
                supplier.getContactNumber(),
                supplier.getEmail(),
                supplier.getAddress()
        );
    }

    @Override
    public void saveSupplier(SupplierDTO supplierDTO) {
        log.info("Saving supplier: {}", supplierDTO.getSupplierName());
        try {
            if (supplierDTO.getSupplierName() == null || supplierDTO.getSupplierName().isEmpty()) {
                throw new RuntimeException("Supplier name cannot be empty");
            }
            Supplier supplier = new Supplier();
            supplier.setSupplierName(supplierDTO.getSupplierName());
            supplier.setContactNumber(supplierDTO.getContactNumber());
            supplier.setEmail(supplierDTO.getEmail());
            supplier.setAddress(supplierDTO.getAddress());
            supplierRepository.save(supplier);
            log.info("Supplier saved successfully");
        } catch (Exception e) {
            log.error("Error saving supplier: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateSupplier(SupplierDTO supplierDTO) {
        log.info("Updating supplier with ID: {}", supplierDTO.getSupplierId());
        try {
            if (supplierDTO.getSupplierName() == null || supplierDTO.getSupplierName().isEmpty()) {
                throw new RuntimeException("Supplier name cannot be empty");
            }
            Optional<Supplier> supplierOptional = supplierRepository.findById(supplierDTO.getSupplierId());
            if (supplierOptional.isEmpty()) {
                throw new RuntimeException("Supplier not found with ID: " + supplierDTO.getSupplierId());
            }
            Supplier supplier = supplierOptional.get();
            supplier.setSupplierName(supplierDTO.getSupplierName());
            supplier.setContactNumber(supplierDTO.getContactNumber());
            supplier.setEmail(supplierDTO.getEmail());
            supplier.setAddress(supplierDTO.getAddress());
            supplierRepository.save(supplier);
            log.info("Supplier updated successfully");
        } catch (Exception e) {
            log.error("Error updating supplier: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<SupplierDTO> getAllSuppliers() {
        log.info("Retrieving all suppliers");
        try {
            List<Supplier> suppliers = supplierRepository.findAll();
            List<SupplierDTO> dtoList = new ArrayList<>();
            for (Supplier supplier : suppliers) {
                dtoList.add(toDTO(supplier));
            }
            return dtoList;
        } catch (Exception e) {
            log.error("Error retrieving all suppliers: {}", e.getMessage());
            throw e;
        }
    }
}
