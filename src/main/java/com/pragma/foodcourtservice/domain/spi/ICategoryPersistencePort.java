package com.pragma.foodcourtservice.domain.spi;

import com.pragma.foodcourtservice.domain.model.Category;

public interface ICategoryPersistencePort {
    Category getCategoryById(Long id);
}
