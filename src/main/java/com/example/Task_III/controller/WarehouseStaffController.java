package com.example.Task_III.controller;

import com.example.Task_III.constant.CommonResponse;
import com.example.Task_III.dto.OrderDTO;
import com.example.Task_III.dto.ProductDTO;
import com.example.Task_III.service.OrderService;
import com.example.Task_III.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.Task_III.constant.ResponseConde.OPERATION_SUCCESS;
import static com.example.Task_III.constant.ResponseMassage.SUCCESS_MASSAGE;

@RestController
@RequestMapping("/api/warehouse-staff")
@CrossOrigin
public class WarehouseStaffController {
    private final ProductService productService;
    private final OrderService orderService;

    public WarehouseStaffController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    // VIEW AND FILTER PRODUCTS

    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getProducts(
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String productName) {
        List<ProductDTO> products = productService.filterProducts(categoryName, productName);
        return new CommonResponse(OPERATION_SUCCESS, products, SUCCESS_MASSAGE);
    }

    // PROCESS ORDERS: Place order and/or Fulfill it

    @PostMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse placeOrder(@RequestBody OrderDTO orderDTO) {
        orderService.saveOrder(orderDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }

    @PutMapping(value = "/orders/{orderId}/process", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse processOrder(@PathVariable Long orderId) {
        orderService.processOrder(orderId);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }

    // VIEW RECENT ORDERS

    @GetMapping(value = "/orders/recent", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getRecentOrders() {
        List<OrderDTO> orders = orderService.getRecentOrders();
        return new CommonResponse(OPERATION_SUCCESS, orders, SUCCESS_MASSAGE);
    }
}
