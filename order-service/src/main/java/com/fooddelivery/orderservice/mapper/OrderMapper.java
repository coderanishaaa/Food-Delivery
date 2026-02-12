package com.fooddelivery.orderservice.mapper;

import com.fooddelivery.orderservice.dto.OrderDto;
import com.fooddelivery.orderservice.dto.OrderItemDto;
import com.fooddelivery.orderservice.entity.Order;
import com.fooddelivery.orderservice.entity.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomerId());
        dto.setRestaurantId(order.getRestaurantId());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        List<OrderItemDto> items = order.getItems().stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
        dto.setItems(items);
        return dto;
    }

    public static OrderItemDto toDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        dto.setMenuItemId(item.getMenuItemId());
        dto.setMenuItemName(item.getMenuItemName());
        dto.setPrice(item.getPrice());
        dto.setQuantity(item.getQuantity());
        return dto;
    }
}

