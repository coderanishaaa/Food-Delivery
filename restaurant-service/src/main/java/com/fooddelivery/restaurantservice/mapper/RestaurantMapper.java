package com.fooddelivery.restaurantservice.mapper;

import com.fooddelivery.restaurantservice.dto.MenuItemDto;
import com.fooddelivery.restaurantservice.dto.RestaurantDto;
import com.fooddelivery.restaurantservice.entity.MenuItem;
import com.fooddelivery.restaurantservice.entity.Restaurant;

import java.util.List;
import java.util.stream.Collectors;

public class RestaurantMapper {

    public static RestaurantDto toDto(Restaurant entity) {
        RestaurantDto dto = new RestaurantDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAddress(entity.getAddress());
        dto.setOwnerId(entity.getOwnerId());
        if (entity.getMenuItems() != null) {
            List<MenuItemDto> items = entity.getMenuItems().stream()
                    .map(RestaurantMapper::toDto)
                    .collect(Collectors.toList());
            dto.setMenuItems(items);
        }
        return dto;
    }

    public static MenuItemDto toDto(MenuItem entity) {
        MenuItemDto dto = new MenuItemDto();
        dto.setId(entity.getId());
        dto.setRestaurantId(entity.getRestaurant().getId());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setDescription(entity.getDescription());
        return dto;
    }
}

