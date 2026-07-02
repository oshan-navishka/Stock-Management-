package com.example.Task_III.service.impl;

import com.example.Task_III.dto.OrderDTO;
import com.example.Task_III.dto.OrderItemDTO;
import com.example.Task_III.entity.Order;
import com.example.Task_III.entity.OrderItem;
import com.example.Task_III.entity.Product;
import com.example.Task_III.repository.OrderRepository;
import com.example.Task_III.repository.ProductRepository;
import com.example.Task_III.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    private OrderDTO toDTO(Order order) {
        if (order == null) return null;
        List<OrderItemDTO> itemsDTO = new ArrayList<>();
        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                Long productId = (item.getProduct() != null) ? item.getProduct().getProductId() : null;
                String productName = (item.getProduct() != null) ? item.getProduct().getProductName() : null;
                itemsDTO.add(new OrderItemDTO(
                        item.getOrderItemId(),
                        item.getQuantity(),
                        item.getPrice(),
                        productId,
                        productName
                ));
            }
        }
        return new OrderDTO(
                order.getOrderId(),
                order.getOrderDate(),
                order.getStatus(),
                order.getCustomerName(),
                itemsDTO
        );
    }

    @Override
    @Transactional
    public void saveOrder(OrderDTO orderDTO) {
        log.info("Saving new order for customer: {}", orderDTO.getCustomerName());
        try {
            if (orderDTO.getCustomerName() == null || orderDTO.getCustomerName().isEmpty()) {
                throw new RuntimeException("Customer name cannot be empty");
            }
            if (orderDTO.getOrderItems() == null || orderDTO.getOrderItems().isEmpty()) {
                throw new RuntimeException("Order must contain at least one item");
            }

            Order order = new Order();
            order.setCustomerName(orderDTO.getCustomerName());
            order.setOrderDate(orderDTO.getOrderDate() != null ? orderDTO.getOrderDate() : LocalDate.now());
            order.setStatus("PENDING");

            List<OrderItem> items = new ArrayList<>();
            for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
                if (itemDTO.getProductId() == null) {
                    throw new RuntimeException("Product ID must be specified for each item");
                }
                if (itemDTO.getQuantity() <= 0) {
                    throw new RuntimeException("Item quantity must be greater than zero");
                }

                Optional<Product> productOpt = productRepository.findById(itemDTO.getProductId());
                if (productOpt.isEmpty()) {
                    throw new RuntimeException("Product not found with ID: " + itemDTO.getProductId());
                }
                Product product = productOpt.get();

                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProduct(product);
                item.setQuantity(itemDTO.getQuantity());
                // Use product's current unit price if not specified in DTO
                item.setPrice(itemDTO.getPrice() > 0 ? itemDTO.getPrice() : product.getPrice());

                items.add(item);
            }
            order.setOrderItems(items);

            orderRepository.save(order);
            log.info("Order saved in PENDING status successfully");
        } catch (Exception e) {
            log.error("Error saving order: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public void processOrder(Long orderId) {
        log.info("Processing/Fulfilling order ID: {}", orderId);
        try {
            Optional<Order> orderOpt = orderRepository.findById(orderId);
            if (orderOpt.isEmpty()) {
                throw new RuntimeException("Order not found with ID: " + orderId);
            }
            Order order = orderOpt.get();
            if ("FULFILLED".equalsIgnoreCase(order.getStatus())) {
                throw new RuntimeException("Order is already fulfilled");
            }

            // Verify and deduct stock for each order item
            for (OrderItem item : order.getOrderItems()) {
                Product product = item.getProduct();
                if (product == null) {
                    throw new RuntimeException("Product associated with order item is missing");
                }
                if (product.getQuantity() < item.getQuantity()) {
                    throw new RuntimeException("Insufficient stock for product: " + product.getProductName() +
                            ". Available: " + product.getQuantity() + ", Required: " + item.getQuantity());
                }
                // Deduct stock
                product.setQuantity(product.getQuantity() - item.getQuantity());
                productRepository.save(product);
            }

            order.setStatus("FULFILLED");
            orderRepository.save(order);
            log.info("Order ID: {} has been FULFILLED and stock levels updated", orderId);
        } catch (Exception e) {
            log.error("Error processing order ID: {}: {}", orderId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<OrderDTO> getRecentOrders() {
        log.info("Retrieving recent orders");
        try {
            List<Order> orders = orderRepository.findAllByOrderByOrderIdDesc();
            List<OrderDTO> dtoList = new ArrayList<>();
            for (Order order : orders) {
                dtoList.add(toDTO(order));
            }
            return dtoList;
        } catch (Exception e) {
            log.error("Error retrieving recent orders: {}", e.getMessage());
            throw e;
        }
    }
}
