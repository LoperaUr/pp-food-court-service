package com.pragma.foodcourtservice.testdata.builders;

import com.pragma.foodcourtservice.domain.model.Dish;

public final class DishBuilder {

    private Long id = 1L;
    private String name = "Plato de prueba";
    private Long categoryId = 1L;
    private String description = "Descripcion del plato";
    private Long restaurantId = 1L;
    private String urlImage = "https://image.test/plato.png";
    private boolean active = false;
    private Long price = 20000L;

    private DishBuilder() {
    }

    public static DishBuilder aDish() {
        return new DishBuilder();
    }

    public DishBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DishBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DishBuilder withCategoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public DishBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DishBuilder withRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
        return this;
    }

    public DishBuilder withUrlImage(String urlImage) {
        this.urlImage = urlImage;
        return this;
    }

    public DishBuilder withActive(boolean active) {
        this.active = active;
        return this;
    }

    public DishBuilder withPrice(Long price) {
        this.price = price;
        return this;
    }

    public Dish build() {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setName(name);
        dish.setCategoryId(categoryId);
        dish.setDescription(description);
        dish.setRestaurantId(restaurantId);
        dish.setUrlImage(urlImage);
        dish.setActive(active);
        dish.setPrice(price);
        return dish;
    }
}

