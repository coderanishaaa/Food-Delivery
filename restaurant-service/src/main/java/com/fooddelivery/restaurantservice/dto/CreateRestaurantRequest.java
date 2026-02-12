package com.fooddelivery.restaurantservice.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateRestaurantRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

