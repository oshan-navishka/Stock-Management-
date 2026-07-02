package com.example.Task_III.controller;

import com.example.Task_III.constant.CommonResponse;
import com.example.Task_III.dto.CategoryDTO;
import com.example.Task_III.dto.ProductDTO;
import com.example.Task_III.service.CategoryService;
import com.example.Task_III.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.example.Task_III.constant.ResponseConde.OPERATION_SUCCESS;
import static com.example.Task_III.constant.ResponseMassage.SUCCESS_MASSAGE;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {
    private final CategoryService categoryService;
    private final ProductService productService;

    public AdminController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    // CATEGORY MANAGEMENT

    @PostMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.saveCategory(categoryDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }

    @PutMapping(value = "/categories/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateCategory(
            @PathVariable Long categoryId,
            @RequestBody CategoryDTO categoryDTO) {
        categoryDTO.setCategoryId(categoryId);
        categoryService.updateCategory(categoryDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }

    // PRODUCT MANAGEMENT

    @PostMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveProduct(@RequestBody ProductDTO productDTO) {
        productService.saveProduct(productDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }

    @PutMapping(value = "/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductDTO productDTO) {
        productDTO.setProductId(productId);
        productService.updateProduct(productDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }

    // ADJUST STOCK QUANTITY

    @PutMapping(value = "/products/{productId}/stock", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateStockQuantity(
            @PathVariable Long productId,
            @RequestParam int quantity) {
        productService.updateStockQuantity(productId, quantity);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }
}
