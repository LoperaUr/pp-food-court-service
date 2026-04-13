package com.pragma.foodcourtservice.testdata.builders;

import com.pragma.foodcourtservice.domain.model.Restaurant;

public final class RestaurantBuilder {

    private Long id = 1L;
    private String name = "Restaurante Test";
    private String nit = "123456789";
    private String address = "Calle 123";
    private String phone = "3001234567";
    private String urlLogo = "https://logo.test";
    private Long ownerId = 2L;

    private RestaurantBuilder() {
    }

    public static RestaurantBuilder aRestaurant() {
        return new RestaurantBuilder();
    }

    public RestaurantBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public RestaurantBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public RestaurantBuilder withNit(String nit) {
        this.nit = nit;
        return this;
    }

    public RestaurantBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public RestaurantBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public RestaurantBuilder withUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
        return this;
    }

    public RestaurantBuilder withOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public Restaurant build() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);
        restaurant.setName(name);
        restaurant.setNit(nit);
        restaurant.setAddress(address);
        restaurant.setPhone(phone);
        restaurant.setUrlLogo(urlLogo);
        restaurant.setOwnerId(ownerId);
        return restaurant;
    }
}

