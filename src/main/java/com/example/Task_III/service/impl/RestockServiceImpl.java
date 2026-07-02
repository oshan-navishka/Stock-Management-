package com.example.Task_III.service.impl;

import com.example.Task_III.dto.RestockDTO;
import com.example.Task_III.entity.Product;
import com.example.Task_III.entity.Restock;
import com.example.Task_III.entity.Supplier;
import com.example.Task_III.repository.ProductRepository;
import com.example.Task_III.repository.RestockRepository;
import com.example.Task_III.repository.SupplierRepository;
import com.example.Task_III.service.RestockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RestockServiceImpl implements RestockService {
    private final RestockRepository restockRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    public RestockServiceImpl(RestockRepository restockRepository,
                              ProductRepository productRepository,
                              SupplierRepository supplierRepository) {
        this.restockRepository = restockRepository;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    private RestockDTO toDTO(Restock restock) {
        if (restock == null) return null;
        Long productId = (restock.getProduct() != null) ? restock.getProduct().getProductId() : null;
        Long supplierId = (restock.getSupplier() != null) ? restock.getSupplier().getSupplierId() : null;
        String productName = (restock.getProduct() != null) ? restock.getProduct().getProductName() : null;
        String supplierName = (restock.getSupplier() != null) ? restock.getSupplier().getSupplierName() : null;

        return new RestockDTO(
                restock.getRestockId(),
                restock.getQuantity(),
                restock.getRestockDate(),
                productId,
                supplierId,
                productName,
                supplierName
        );
    }

    @Override
    @Transactional
    public void saveRestock(RestockDTO restockDTO) {
        log.info("Recording restocking for product ID: {}", restockDTO.getProductId());
        try {
            if (restockDTO.getProductId() == null) {
                throw new RuntimeException("Product ID cannot be null");
            }
            if (restockDTO.getSupplierId() == null) {
                throw new RuntimeException("Supplier ID cannot be null");
            }
            if (restockDTO.getQuantity() <= 0) {
                throw new RuntimeException("Restock quantity must be greater than zero");
            }

            Optional<Product> productOpt = productRepository.findById(restockDTO.getProductId());
            if (productOpt.isEmpty()) {
                throw new RuntimeException("Product not found with ID: " + restockDTO.getProductId());
            }

            Optional<Supplier> supplierOpt = supplierRepository.findById(restockDTO.getSupplierId());
            if (supplierOpt.isEmpty()) {
                throw new RuntimeException("Supplier not found with ID: " + restockDTO.getSupplierId());
            }

            Product product = productOpt.get();
            Supplier supplier = supplierOpt.get();

            Restock restock = new Restock();
            restock.setQuantity(restockDTO.getQuantity());
            restock.setRestockDate(restockDTO.getRestockDate() != null ? restockDTO.getRestockDate() : LocalDate.now());
            restock.setProduct(product);
            restock.setSupplier(supplier);

            // Save restocking record
            restockRepository.save(restock);

            // Update product stock quantity
            product.setQuantity(product.getQuantity() + restockDTO.getQuantity());
            productRepository.save(product);

            log.info("Restocking recorded and product quantity updated successfully");
        } catch (Exception e) {
            log.error("Error recording restocking: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<RestockDTO> getAllRestocks() {
        log.info("Retrieving all restocking records");
        try {
            List<Restock> restocks = restockRepository.findAll();
            List<RestockDTO> dtoList = new ArrayList<>();
            for (Restock restock : restocks) {
                dtoList.add(toDTO(restock));
            }
            return dtoList;
        } catch (Exception e) {
            log.error("Error retrieving restocking records: {}", e.getMessage());
            throw e;
        }
    }
}
