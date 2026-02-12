package com.fooddelivery.restaurantservice.controller;

import com.fooddelivery.restaurantservice.dto.CreateMenuItemRequest;
import com.fooddelivery.restaurantservice.dto.CreateRestaurantRequest;
import com.fooddelivery.restaurantservice.dto.MenuItemDto;
import com.fooddelivery.restaurantservice.dto.RestaurantDto;
import com.fooddelivery.restaurantservice.service.RestaurantService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> getAll() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/owner")
    public ResponseEntity<List<RestaurantDto>> getForOwner(HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(restaurantService.getRestaurantsForOwner(userId));
    }

    @PostMapping
    public ResponseEntity<RestaurantDto> create(@Valid @RequestBody CreateRestaurantRequest body,
                                                HttpServletRequest request) {
        Long userId = getUserId(request);
        Set<String> roles = getRoles(request);
        if (!RestaurantService.hasAnyRole(roles, "RESTAURANT_OWNER", "ADMIN")) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(restaurantService.createRestaurant(body, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDto> update(@PathVariable Long id,
                                                @Valid @RequestBody CreateRestaurantRequest body,
                                                HttpServletRequest request) {
        Long userId = getUserId(request);
        Set<String> roles = getRoles(request);
        boolean isAdmin = roles.contains("ADMIN");
        if (!RestaurantService.hasAnyRole(roles, "RESTAURANT_OWNER", "ADMIN")) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, body, userId, isAdmin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserId(request);
        Set<String> roles = getRoles(request);
        boolean isAdmin = roles.contains("ADMIN");
        if (!RestaurantService.hasAnyRole(roles, "RESTAURANT_OWNER", "ADMIN")) {
            return ResponseEntity.status(403).build();
        }
        restaurantService.deleteRestaurant(id, userId, isAdmin);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<List<MenuItemDto>> getMenu(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getMenuForRestaurant(id));
    }

    @PostMapping("/{id}/menu")
    public ResponseEntity<MenuItemDto> addMenuItem(@PathVariable Long id,
                                                   @Valid @RequestBody CreateMenuItemRequest body,
                                                   HttpServletRequest request) {
        Long userId = getUserId(request);
        Set<String> roles = getRoles(request);
        boolean isAdmin = roles.contains("ADMIN");
        if (!RestaurantService.hasAnyRole(roles, "RESTAURANT_OWNER", "ADMIN")) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(restaurantService.addMenuItem(id, body, userId, isAdmin));
    }

    @DeleteMapping("/menu/{menuItemId}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long menuItemId, HttpServletRequest request) {
        Long userId = getUserId(request);
        Set<String> roles = getRoles(request);
        boolean isAdmin = roles.contains("ADMIN");
        if (!RestaurantService.hasAnyRole(roles, "RESTAURANT_OWNER", "ADMIN")) {
            return ResponseEntity.status(403).build();
        }
        restaurantService.deleteMenuItem(menuItemId, userId, isAdmin);
        return ResponseEntity.noContent().build();
    }

    private Long getUserId(HttpServletRequest request) {
        String header = request.getHeader("X-User-Id");
        return header != null ? Long.parseLong(header) : null;
    }

    private Set<String> getRoles(HttpServletRequest request) {
        String header = request.getHeader("X-User-Roles");
        if (header == null || header.isEmpty()) {
            return new HashSet<>();
        }
        String trimmed = header.replace("[", "").replace("]", "");
        if (trimmed.isEmpty()) {
            return new HashSet<>();
        }
        return Arrays.stream(trimmed.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
    }
}

