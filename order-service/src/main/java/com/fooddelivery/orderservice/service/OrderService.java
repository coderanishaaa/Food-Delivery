package com.fooddelivery.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddelivery.orderservice.dto.OrderDto;
import com.fooddelivery.orderservice.dto.OrderEvent;
import com.fooddelivery.orderservice.dto.PlaceOrderRequest;
import com.fooddelivery.orderservice.entity.Order;
import com.fooddelivery.orderservice.entity.OrderItem;
import com.fooddelivery.orderservice.entity.OrderStatus;
import com.fooddelivery.orderservice.mapper.OrderMapper;
import com.fooddelivery.orderservice.repository.OrderRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository,
                        KafkaTemplate<String, String> kafkaTemplate,
                        ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public OrderDto placeOrder(PlaceOrderRequest request, Long customerId) {
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setRestaurantId(request.getRestaurantId());
        order.setStatus(OrderStatus.PENDING_PAYMENT);

        BigDecimal total = BigDecimal.ZERO;
        for (PlaceOrderRequest.OrderItemRequest itemReq : request.getItems()) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setMenuItemId(itemReq.getMenuItemId());
            item.setMenuItemName(itemReq.getName());
            item.setPrice(itemReq.getPrice());
            item.setQuantity(itemReq.getQuantity());
            order.getItems().add(item);
            total = total.add(itemReq.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())));
        }
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);
        publishEvent(buildEvent("ORDER_CREATED", saved));
        return OrderMapper.toDto(saved);
    }

    public OrderDto getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return OrderMapper.toDto(order);
    }

    @Transactional
    public void updateStatusFromEvent(String type, Long orderId) {
        orderRepository.findById(orderId).ifPresent(order -> {
            if ("ORDER_PAID".equals(type)) {
                order.setStatus(OrderStatus.PAID);
            } else if ("ORDER_ASSIGNED".equals(type)) {
                order.setStatus(OrderStatus.ASSIGNED);
            } else if ("ORDER_DELIVERED".equals(type)) {
                order.setStatus(OrderStatus.DELIVERED);
            }
            orderRepository.save(order);
        });
    }

    private void publishEvent(OrderEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("order-events", event.getOrderId().toString(), payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize order event", e);
        }
    }

    private OrderEvent buildEvent(String type, Order order) {
        OrderEvent event = new OrderEvent();
        event.setType(type);
        event.setOrderId(order.getId());
        event.setCustomerId(order.getCustomerId());
        event.setRestaurantId(order.getRestaurantId());
        event.setTotalAmount(order.getTotalAmount());
        event.setCreatedAt(Instant.now());
        return event;
    }
}

