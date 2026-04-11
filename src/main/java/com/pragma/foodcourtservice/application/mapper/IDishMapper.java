package com.pragma.foodcourtservice.application.mapper;

import com.pragma.foodcourtservice.application.dto.DishDTO;
import com.pragma.foodcourtservice.domain.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IDishMapper {

    Dish toDish(DishDTO dishDTO);

}
