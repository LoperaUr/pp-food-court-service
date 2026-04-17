package com.pragma.foodcourtservice.application.handler;

import com.pragma.foodcourtservice.application.dto.DishDTO;
import com.pragma.foodcourtservice.application.dto.DishStatusDTO;
import com.pragma.foodcourtservice.application.dto.PageResponseDTO;
import com.pragma.foodcourtservice.application.mapper.IDishMapper;
import com.pragma.foodcourtservice.domain.api.IDishServicePort;
import com.pragma.foodcourtservice.domain.model.Dish;
import com.pragma.foodcourtservice.domain.model.PageModel;
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

    @Override
    public void updateDishStatus(Long id, DishStatusDTO dishStatusDTO) {
        dishServicePort.updateDishStatus(id, dishStatusDTO.isActive());
    }

    @Override
    public PageResponseDTO<DishDTO> getDishesByRestaurant(Long id, Long categoryId, int page, int size) {
        PageModel<Dish> dishes = dishServicePort.getDishesByRestaurant(id, categoryId, page, size);
        return dishMapper.toPageResponseDTO(dishes);
    }

}
