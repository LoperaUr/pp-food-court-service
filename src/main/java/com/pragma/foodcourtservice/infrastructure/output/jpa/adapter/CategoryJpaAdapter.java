package com.pragma.foodcourtservice.infrastructure.output.jpa.adapter;


import com.pragma.foodcourtservice.domain.model.Category;
import com.pragma.foodcourtservice.domain.spi.ICategoryPersistencePort;
import com.pragma.foodcourtservice.infrastructure.output.jpa.entity.CategoryEntity;
import com.pragma.foodcourtservice.infrastructure.output.jpa.mapper.ICategoryEntityMapper;
import com.pragma.foodcourtservice.infrastructure.output.jpa.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryJpaAdapter implements ICategoryPersistencePort {

    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;

    @Override
    public Category getCategoryById(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id).orElse(null);
        return categoryEntityMapper.toCategory(categoryEntity);
    }
}
