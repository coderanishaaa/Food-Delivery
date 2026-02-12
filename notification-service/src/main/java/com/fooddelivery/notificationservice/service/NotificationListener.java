package com.fooddelivery.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddelivery.notificationservice.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);

    private final ObjectMapper objectMapper;

    public NotificationListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Consumes order-events and logs notification messages.
     * In a real system, this is where you'd send email/SMS/push.
     */
    @KafkaListener(topics = "order-events", groupId = "notification-service-group")
    public void onOrderEvent(String message) throws JsonProcessingException {
        OrderEvent event = objectMapper.readValue(message, OrderEvent.class);
        String msg = switch (event.getType()) {
            case "ORDER_CREATED" -> "New order created: #" + event.getOrderId();
            case "ORDER_PAID" -> "Order paid: #" + event.getOrderId();
            case "ORDER_ASSIGNED" -> "Order assigned to delivery: #" + event.getOrderId();
            case "ORDER_DELIVERED" -> "Order delivered: #" + event.getOrderId();
            default -> "Unknown event " + event.getType() + " for order #" + event.getOrderId();
        };
        log.info("[Notification] {}", msg);
    }
}

