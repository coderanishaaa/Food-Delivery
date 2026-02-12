package com.fooddelivery.restaurantservice.dto;

import java.math.BigDecimal;

public class MenuItemDto {

    private Long id;
    private Long restaurantId;
    private String name;
    private BigDecimal price;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
       this.id = id;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

