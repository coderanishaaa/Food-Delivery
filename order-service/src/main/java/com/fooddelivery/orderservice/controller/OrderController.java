package com.fooddelivery.orderservice.controller;

import com.fooddelivery.orderservice.dto.OrderDto;
import com.fooddelivery.orderservice.dto.PlaceOrderRequest;
import com.fooddelivery.orderservice.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDto> place(@Valid @RequestBody PlaceOrderRequest request,
                                          HttpServletRequest servletRequest) {
        String userIdHeader = servletRequest.getHeader("X-User-Id");
        Long customerId = userIdHeader != null ? Long.parseLong(userIdHeader) : null;
        return ResponseEntity.ok(orderService.placeOrder(request, customerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }
}

