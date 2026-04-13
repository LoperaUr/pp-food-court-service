package com.pragma.foodcourtservice.testdata.builders;

import com.pragma.foodcourtservice.domain.model.Category;

public final class CategoryBuilder {

    private Long id = 1L;
    private String name = "Categoria test";

    private CategoryBuilder() {
    }

    public static CategoryBuilder aCategory() {
        return new CategoryBuilder();
    }

    public CategoryBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CategoryBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Category build() {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;
    }
}

