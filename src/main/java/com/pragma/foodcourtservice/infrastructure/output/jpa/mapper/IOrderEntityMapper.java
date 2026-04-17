package com.pragma.foodcourtservice.infrastructure.output.jpa.mapper;

import com.pragma.foodcourtservice.domain.model.Order;
import com.pragma.foodcourtservice.domain.model.OrderDish;
import com.pragma.foodcourtservice.infrastructure.output.jpa.entity.OrderDishEntity;
import com.pragma.foodcourtservice.infrastructure.output.jpa.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IOrderEntityMapper {
    OrderEntity toEntity(Order order);

    @Mapping(target = "order", ignore = true)
    OrderDishEntity toEntity(OrderDish orderDish);
}
