package com.example.Task_III.service.impl;

import com.example.Task_III.dto.ProductDTO;
import com.example.Task_III.entity.Category;
import com.example.Task_III.entity.Product;
import com.example.Task_III.entity.Supplier;
import com.example.Task_III.repository.CategoryRepository;
import com.example.Task_III.repository.ProductRepository;
import com.example.Task_III.repository.SupplierRepository;
import com.example.Task_III.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
    }

    private ProductDTO toDTO(Product product) {
        if (product == null) return null;
        Long categoryId = (product.getCategory() != null) ? product.getCategory().getCategoryId() : null;
        Long supplierId = (product.getSupplier() != null) ? product.getSupplier().getSupplierId() : null;
        String categoryName = (product.getCategory() != null) ? product.getCategory().getCategoryName() : null;
        String supplierName = (product.getSupplier() != null) ? product.getSupplier().getSupplierName() : null;

        return new ProductDTO(
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                categoryId,
                supplierId,
                categoryName,
                supplierName
        );
    }

    @Override
    public void saveProduct(ProductDTO productDTO) {
        log.info("Saving product: {}", productDTO.getProductName());
        try {
            if (productDTO.getProductName() == null || productDTO.getProductName().isEmpty()) {
                throw new RuntimeException("Product name cannot be empty");
            }
            Product product = new Product();
            product.setProductName(productDTO.getProductName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setQuantity(productDTO.getQuantity());

            if (productDTO.getCategoryId() != null) {
                Optional<Category> categoryOpt = categoryRepository.findById(productDTO.getCategoryId());
                if (categoryOpt.isEmpty()) {
                    throw new RuntimeException("Category not found with ID: " + productDTO.getCategoryId());
                }
                product.setCategory(categoryOpt.get());
            }

            if (productDTO.getSupplierId() != null) {
                Optional<Supplier> supplierOpt = supplierRepository.findById(productDTO.getSupplierId());
                if (supplierOpt.isEmpty()) {
                    throw new RuntimeException("Supplier not found with ID: " + productDTO.getSupplierId());
                }
                product.setSupplier(supplierOpt.get());
            }

            productRepository.save(product);
            log.info("Product saved successfully");
        } catch (Exception e) {
            log.error("Error saving product: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateProduct(ProductDTO productDTO) {
        log.info("Updating product with ID: {}", productDTO.getProductId());
        try {
            if (productDTO.getProductName() == null || productDTO.getProductName().isEmpty()) {
                throw new RuntimeException("Product name cannot be empty");
            }
            Optional<Product> productOpt = productRepository.findById(productDTO.getProductId());
            if (productOpt.isEmpty()) {
                throw new RuntimeException("Product not found with ID: " + productDTO.getProductId());
            }
            Product product = productOpt.get();
            product.setProductName(productDTO.getProductName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setQuantity(productDTO.getQuantity());

            if (productDTO.getCategoryId() != null) {
                Optional<Category> categoryOpt = categoryRepository.findById(productDTO.getCategoryId());
                if (categoryOpt.isEmpty()) {
                    throw new RuntimeException("Category not found with ID: " + productDTO.getCategoryId());
                }
                product.setCategory(categoryOpt.get());
            } else {
                product.setCategory(null);
            }

            if (productDTO.getSupplierId() != null) {
                Optional<Supplier> supplierOpt = supplierRepository.findById(productDTO.getSupplierId());
                if (supplierOpt.isEmpty()) {
                    throw new RuntimeException("Supplier not found with ID: " + productDTO.getSupplierId());
                }
                product.setSupplier(supplierOpt.get());
            } else {
                product.setSupplier(null);
            }

            productRepository.save(product);
            log.info("Product updated successfully");
        } catch (Exception e) {
            log.error("Error updating product: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateStockQuantity(Long productId, int quantity) {
        log.info("Adjusting stock quantity for product ID: {} to {}", productId, quantity);
        try {
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                throw new RuntimeException("Product not found with ID: " + productId);
            }
            Product product = productOpt.get();
            if (quantity < 0) {
                throw new RuntimeException("Stock quantity cannot be negative");
            }
            product.setQuantity(quantity);
            productRepository.save(product);
            log.info("Stock quantity updated successfully");
        } catch (Exception e) {
            log.error("Error adjusting stock quantity: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<ProductDTO> getLowStockProducts(int threshold) {
        log.info("Retrieving products below stock threshold: {}", threshold);
        try {
            List<Product> products = productRepository.findByQuantityLessThan(threshold);
            List<ProductDTO> dtoList = new ArrayList<>();
            for (Product product : products) {
                dtoList.add(toDTO(product));
            }
            return dtoList;
        } catch (Exception e) {
            log.error("Error retrieving low-stock products: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<ProductDTO> filterProducts(String categoryName, String productName) {
        log.info("Filtering products by categoryName: {} and productName: {}", categoryName, productName);
        try {
            String queryCategoryName = (categoryName != null) ? categoryName.trim() : "";
            String queryProductName = (productName != null) ? productName.trim() : "";
            List<Product> products = productRepository.filterProducts(queryCategoryName, queryProductName);
            List<ProductDTO> dtoList = new ArrayList<>();
            for (Product product : products) {
                dtoList.add(toDTO(product));
            }
            return dtoList;
        } catch (Exception e) {
            log.error("Error filtering products: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        log.info("Retrieving all products");
        try {
            List<Product> products = productRepository.findAll();
            List<ProductDTO> dtoList = new ArrayList<>();
            for (Product product : products) {
                dtoList.add(toDTO(product));
            }
            return dtoList;
        } catch (Exception e) {
            log.error("Error retrieving all products: {}", e.getMessage());
            throw e;
        }
    }
}
