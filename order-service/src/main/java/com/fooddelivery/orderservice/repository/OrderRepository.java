package com.fooddelivery.orderservice.repository;

import com.fooddelivery.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

