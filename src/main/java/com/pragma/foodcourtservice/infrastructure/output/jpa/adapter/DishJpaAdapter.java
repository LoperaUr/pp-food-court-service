package com.pragma.foodcourtservice.infrastructure.output.jpa.adapter;

import com.pragma.foodcourtservice.domain.model.Dish;
import com.pragma.foodcourtservice.domain.model.PageModel;
import com.pragma.foodcourtservice.domain.spi.IDishPersistencePort;
import com.pragma.foodcourtservice.infrastructure.output.jpa.entity.DishEntity;
import com.pragma.foodcourtservice.infrastructure.output.jpa.mapper.IDishEntityMapper;
import com.pragma.foodcourtservice.infrastructure.output.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Override
    public PageModel<Dish> getDishesByRestaurant(Long id, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<DishEntity> pageResult = dishRepository.findByRestaurantIdAndActiveIsTrue(id, pageRequest);

        return dishEntityMapper.toPageModel(pageResult);
    }

    @Override
    public PageModel<Dish> getDishesByRestaurantAndCategoryId(Long id, Long categoryId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<DishEntity> pageResult = dishRepository.findByRestaurantIdAndCategoryIdAndActiveIsTrue(id, categoryId, pageRequest);

        return dishEntityMapper.toPageModel(pageResult);
    }
}
