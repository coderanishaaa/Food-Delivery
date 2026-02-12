package com.fooddelivery.deliveryservice.mapper;

import com.fooddelivery.deliveryservice.dto.DeliveryDto;
import com.fooddelivery.deliveryservice.entity.Delivery;

public class DeliveryMapper {

    public static DeliveryDto toDto(Delivery entity) {
        DeliveryDto dto = new DeliveryDto();
        dto.setId(entity.getId());
        dto.setOrderId(entity.getOrderId());
        dto.setDeliveryAgentId(entity.getDeliveryAgentId());
        dto.setStatus(entity.getStatus());
        dto.setTrackingCode(entity.getTrackingCode());
        return dto;
    }
}

