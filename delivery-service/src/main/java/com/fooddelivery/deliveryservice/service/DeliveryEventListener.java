package com.fooddelivery.deliveryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddelivery.deliveryservice.dto.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DeliveryEventListener {

    private final DeliveryService deliveryService;
    private final ObjectMapper objectMapper;

    public DeliveryEventListener(DeliveryService deliveryService, ObjectMapper objectMapper) {
        this.deliveryService = deliveryService;
        this.objectMapper = objectMapper;
    }

    /**
     * Listens to ORDER_PAID events and creates delivery records.
     */
    @KafkaListener(topics = "order-events", groupId = "delivery-service-group")
    public void onOrderPaid(String message) throws JsonProcessingException {
        OrderEvent event = objectMapper.readValue(message, OrderEvent.class);
        if (!"ORDER_PAID".equals(event.getType())) {
            return;
        }
        deliveryService.createFromPaidEvent(event);
    }
}

