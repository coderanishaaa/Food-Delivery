package com.fooddelivery.restaurantservice.service;

import com.fooddelivery.restaurantservice.dto.CreateMenuItemRequest;
import com.fooddelivery.restaurantservice.dto.CreateRestaurantRequest;
import com.fooddelivery.restaurantservice.dto.MenuItemDto;
import com.fooddelivery.restaurantservice.dto.RestaurantDto;
import com.fooddelivery.restaurantservice.entity.MenuItem;
import com.fooddelivery.restaurantservice.entity.Restaurant;
import com.fooddelivery.restaurantservice.mapper.RestaurantMapper;
import com.fooddelivery.restaurantservice.repository.MenuItemRepository;
import com.fooddelivery.restaurantservice.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public RestaurantService(RestaurantRepository restaurantRepository,
                             MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional
    public RestaurantDto createRestaurant(CreateRestaurantRequest request, Long ownerId) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        restaurant.setOwnerId(ownerId);
        return RestaurantMapper.toDto(restaurantRepository.save(restaurant));
    }

    public List<RestaurantDto> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(RestaurantMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RestaurantDto> getRestaurantsForOwner(Long ownerId) {
        return restaurantRepository.findByOwnerId(ownerId).stream()
                .map(RestaurantMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public RestaurantDto updateRestaurant(Long id, CreateRestaurantRequest request, Long ownerId, boolean isAdmin) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        if (!isAdmin && !restaurant.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException("Not allowed to modify this restaurant");
        }
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        return RestaurantMapper.toDto(restaurantRepository.save(restaurant));
    }

    @Transactional
    public void deleteRestaurant(Long id, Long ownerId, boolean isAdmin) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        if (!isAdmin && !restaurant.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException("Not allowed to delete this restaurant");
        }
        restaurantRepository.delete(restaurant);
    }

    @Transactional
    public MenuItemDto addMenuItem(Long restaurantId, CreateMenuItemRequest request, Long ownerId, boolean isAdmin) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        if (!isAdmin && !restaurant.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException("Not allowed to modify this restaurant's menu");
        }
        MenuItem item = new MenuItem();
        item.setRestaurant(restaurant);
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setDescription(request.getDescription());
        return RestaurantMapper.toDto(menuItemRepository.save(item));
    }

    public List<MenuItemDto> getMenuForRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId).stream()
                .map(RestaurantMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMenuItem(Long menuItemId, Long ownerId, boolean isAdmin) {
        MenuItem item = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found"));
        Restaurant restaurant = item.getRestaurant();
        if (!isAdmin && !restaurant.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException("Not allowed to delete this menu item");
        }
        menuItemRepository.delete(item);
    }

    public static boolean hasAnyRole(Set<String> roles, String... required) {
        for (String r : required) {
            if (roles.contains(r)) {
                return true;
            }
        }
        return false;
    }
}

