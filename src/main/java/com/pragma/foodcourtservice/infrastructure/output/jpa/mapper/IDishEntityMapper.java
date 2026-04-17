package com.pragma.foodcourtservice.infrastructure.output.jpa.mapper;

import com.pragma.foodcourtservice.domain.model.Dish;
import com.pragma.foodcourtservice.domain.model.PageModel;
import com.pragma.foodcourtservice.infrastructure.output.jpa.entity.DishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IDishEntityMapper {
    DishEntity toEntity(Dish restaurant);

    Dish toDish(DishEntity dishEntity);

    @Mapping(target = "pageNumber", source = "number")
    @Mapping(target = "pageSize", source = "size")
    @Mapping(target = "totalElements", source = "totalElements")
    @Mapping(target = "totalPages", source = "totalPages")
    @Mapping(target = "content", source = "content")
    PageModel<Dish> toPageModel(Page<DishEntity> pageResult);
}
