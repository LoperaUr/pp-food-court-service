package com.pragma.foodcourtservice.application.mapper;

import com.pragma.foodcourtservice.application.dto.PageResponseDTO;
import com.pragma.foodcourtservice.application.dto.RestaurantDTO;
import com.pragma.foodcourtservice.domain.model.PageModel;
import com.pragma.foodcourtservice.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IRestaurantMapper {

    Restaurant toEntity(RestaurantDTO restaurantDTO);

    PageResponseDTO<RestaurantDTO> toPageResponseDTO(PageModel<Restaurant> restaurantPage);
}
