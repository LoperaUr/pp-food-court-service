package com.pragma.foodcourtservice.application.handler;

import com.pragma.foodcourtservice.application.dto.DishDTO;
import com.pragma.foodcourtservice.application.mapper.IDishMapper;
import com.pragma.foodcourtservice.domain.api.IDishServicePort;
import com.pragma.foodcourtservice.domain.model.Dish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DishHandler implements IDishHandler {

    private final IDishMapper dishMapper;
    private final IDishServicePort dishServicePort;

    @Override
    public void createDish(DishDTO dishDTO) {
        Dish dish = dishMapper.toDish(dishDTO);
        dishServicePort.createDish(dish);
    }

    @Override
    public void updateDish(Long id, DishDTO dishDTO) {
        Dish dish = dishMapper.toDish(dishDTO);
        dish.setId(id);
        dishServicePort.updateDish(dish);
    }


}
