package com.fooddelivery.paymentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddelivery.paymentservice.dto.OrderEvent;
import com.fooddelivery.paymentservice.entity.Payment;
import com.fooddelivery.paymentservice.entity.PaymentStatus;
import com.fooddelivery.paymentservice.repository.PaymentRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PaymentProcessor {

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PaymentProcessor(PaymentRepository paymentRepository,
                            KafkaTemplate<String, String> kafkaTemplate,
                            ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Listens to ORDER_CREATED events and simulates payment processing,
     * then publishes ORDER_PAID events.
     */
    @KafkaListener(topics = "order-events", groupId = "payment-service-group")
    public void onOrderEvent(String message) throws JsonProcessingException {
        OrderEvent event = objectMapper.readValue(message, OrderEvent.class);
        if (!"ORDER_CREATED".equals(event.getType())) {
            return;
        }

        Payment payment = new Payment();
        payment.setOrderId(event.getOrderId());
        payment.setCustomerId(event.getCustomerId());
        payment.setAmount(event.getTotalAmount());
        payment.setStatus(PaymentStatus.SUCCESS); // always success for simulation
        payment.setCreatedAt(Instant.now());
        paymentRepository.save(payment);

        OrderEvent paid = new OrderEvent();
        paid.setType("ORDER_PAID");
        paid.setOrderId(event.getOrderId());
        paid.setCustomerId(event.getCustomerId());
        paid.setRestaurantId(event.getRestaurantId());
        paid.setTotalAmount(event.getTotalAmount());
        paid.setCreatedAt(Instant.now());

        String payload = objectMapper.writeValueAsString(paid);
        kafkaTemplate.send("order-events", paid.getOrderId().toString(), payload);
    }
}

