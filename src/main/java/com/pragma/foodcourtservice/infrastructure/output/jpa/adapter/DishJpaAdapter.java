package com.pragma.foodcourtservice.infrastructure.output.jpa.adapter;

import com.pragma.foodcourtservice.domain.model.Dish;
import com.pragma.foodcourtservice.domain.spi.IDishPersistencePort;
import com.pragma.foodcourtservice.infrastructure.output.jpa.entity.DishEntity;
import com.pragma.foodcourtservice.infrastructure.output.jpa.mapper.IDishEntityMapper;
import com.pragma.foodcourtservice.infrastructure.output.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

    @Override
    public void saveDish(Dish dish) {
        DishEntity dishEntity = dishEntityMapper.toEntity(dish);
        dishRepository.save(dishEntity);
    }

    @Override
    public Dish getDishById(Long id) {
        DishEntity dishEntity = dishRepository.findById(id).orElse(null);
        return dishEntityMapper.toDish(dishEntity);
    }
}
