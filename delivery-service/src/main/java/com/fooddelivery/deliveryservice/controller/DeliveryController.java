package com.fooddelivery.deliveryservice.controller;

import com.fooddelivery.deliveryservice.dto.DeliveryDto;
import com.fooddelivery.deliveryservice.entity.DeliveryStatus;
import com.fooddelivery.deliveryservice.service.DeliveryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<DeliveryDto> byOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(deliveryService.findByOrderId(orderId));
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<DeliveryDto> updateStatus(@PathVariable Long id,
                                                    @RequestParam DeliveryStatus status,
                                                    HttpServletRequest request) {
        Set<String> roles = parseRoles(request.getHeader("X-User-Roles"));
        if (!roles.contains("DELIVERY_AGENT") && !roles.contains("ADMIN")) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(deliveryService.updateStatus(id, status));
    }

    private Set<String> parseRoles(String header) {
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

