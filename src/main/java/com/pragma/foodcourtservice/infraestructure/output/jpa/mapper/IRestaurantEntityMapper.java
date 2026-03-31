package com.pragma.foodcourtservice.infraestructure.output.jpa.mapper;

import com.pragma.foodcourtservice.domain.model.Restaurant;
import com.pragma.foodcourtservice.infraestructure.output.jpa.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)

public interface IRestaurantEntityMapper {
    RestaurantEntity toEntity(Restaurant restaurant);
}
