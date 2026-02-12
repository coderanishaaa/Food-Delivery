package com.fooddelivery.deliveryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddelivery.deliveryservice.dto.DeliveryDto;
import com.fooddelivery.deliveryservice.dto.OrderEvent;
import com.fooddelivery.deliveryservice.entity.Delivery;
import com.fooddelivery.deliveryservice.entity.DeliveryStatus;
import com.fooddelivery.deliveryservice.mapper.DeliveryMapper;
import com.fooddelivery.deliveryservice.repository.DeliveryRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public DeliveryService(DeliveryRepository deliveryRepository,
                           KafkaTemplate<String, String> kafkaTemplate,
                           ObjectMapper objectMapper) {
        this.deliveryRepository = deliveryRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public DeliveryDto createFromPaidEvent(OrderEvent event) {
        Delivery delivery = new Delivery();
        delivery.setOrderId(event.getOrderId());
        // Demo: assign a static delivery agent id
        delivery.setDeliveryAgentId(1001L);
        delivery.setStatus(DeliveryStatus.ASSIGNED);
        delivery.setTrackingCode("TRK-" + event.getOrderId());
        Delivery saved = deliveryRepository.save(delivery);

        publishStatusEvent("ORDER_ASSIGNED", saved);
        return DeliveryMapper.toDto(saved);
    }

    public DeliveryDto findByOrderId(Long orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found for order " + orderId));
        return DeliveryMapper.toDto(delivery);
    }

    @Transactional
    public DeliveryDto updateStatus(Long id, DeliveryStatus status) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found"));
        delivery.setStatus(status);
        Delivery saved = deliveryRepository.save(delivery);

        if (status == DeliveryStatus.DELIVERED) {
            publishStatusEvent("ORDER_DELIVERED", saved);
        }
        return DeliveryMapper.toDto(saved);
    }

    private void publishStatusEvent(String type, Delivery delivery) {
        OrderEvent event = new OrderEvent();
        event.setType(type);
        event.setOrderId(delivery.getOrderId());
        event.setCreatedAt(Instant.now());
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("order-events", delivery.getOrderId().toString(), payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize delivery event", e);
        }
    }
}

