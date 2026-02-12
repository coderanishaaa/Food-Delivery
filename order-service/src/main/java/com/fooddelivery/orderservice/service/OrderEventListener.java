package com.fooddelivery.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddelivery.orderservice.dto.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventListener {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public OrderEventListener(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "order-events", groupId = "order-service-group")
    public void onOrderEvent(String message) throws JsonProcessingException {
        OrderEvent event = objectMapper.readValue(message, OrderEvent.class);
        if ("ORDER_PAID".equals(event.getType())
                || "ORDER_ASSIGNED".equals(event.getType())
                || "ORDER_DELIVERED".equals(event.getType())) {
            orderService.updateStatusFromEvent(event.getType(), event.getOrderId());
        }
    }
}

