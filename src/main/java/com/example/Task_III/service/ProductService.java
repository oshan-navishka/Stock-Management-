package com.example.Task_III.service;

import com.example.Task_III.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    void saveProduct(ProductDTO productDTO);
    void updateProduct(ProductDTO productDTO);
    void updateStockQuantity(Long productId, int quantity);
    List<ProductDTO> getLowStockProducts(int threshold);
    List<ProductDTO> filterProducts(String categoryName, String productName);
    List<ProductDTO> getAllProducts();
}
