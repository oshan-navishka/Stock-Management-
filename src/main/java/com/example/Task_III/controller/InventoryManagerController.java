package com.example.Task_III.controller;

import com.example.Task_III.constant.CommonResponse;
import com.example.Task_III.dto.ProductDTO;
import com.example.Task_III.dto.RestockDTO;
import com.example.Task_III.dto.SupplierDTO;
import com.example.Task_III.service.ProductService;
import com.example.Task_III.service.RestockService;
import com.example.Task_III.service.SupplierService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.Task_III.constant.ResponseConde.OPERATION_SUCCESS;
import static com.example.Task_III.constant.ResponseMassage.SUCCESS_MASSAGE;

@RestController
@RequestMapping("/api/inventory-manager")
@CrossOrigin
public class InventoryManagerController {
    private final ProductService productService;
    private final SupplierService supplierService;
    private final RestockService restockService;

    public InventoryManagerController(ProductService productService,
                                      SupplierService supplierService,
                                      RestockService restockService) {
        this.productService = productService;
        this.supplierService = supplierService;
        this.restockService = restockService;
    }

    // VIEW LOW-STOCK PRODUCTS

    @GetMapping(value = "/products/low-stock", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getLowStockProducts() {
        List<ProductDTO> products = productService.getLowStockProducts(100);
        return new CommonResponse(OPERATION_SUCCESS, products, SUCCESS_MASSAGE);
    }

    // SUPPLIER MANAGEMENT

    @PostMapping(value = "/suppliers", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveSupplier(@RequestBody SupplierDTO supplierDTO) {
        supplierService.saveSupplier(supplierDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }

    @PutMapping(value = "/suppliers/{supplierId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateSupplier(
            @PathVariable Long supplierId,
            @RequestBody SupplierDTO supplierDTO) {
        supplierDTO.setSupplierId(supplierId);
        supplierService.updateSupplier(supplierDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }

    // RECORD RESTOCKING

    @PostMapping(value = "/restocks", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse recordRestocking(@RequestBody RestockDTO restockDTO) {
        restockService.saveRestock(restockDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }
}
