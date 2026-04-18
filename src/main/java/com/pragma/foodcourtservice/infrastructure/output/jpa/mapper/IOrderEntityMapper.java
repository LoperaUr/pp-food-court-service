package com.pragma.foodcourtservice.infrastructure.output.jpa.mapper;

import com.pragma.foodcourtservice.domain.model.Order;
import com.pragma.foodcourtservice.domain.model.OrderDish;
import com.pragma.foodcourtservice.domain.model.PageModel;
import com.pragma.foodcourtservice.infrastructure.output.jpa.entity.OrderDishEntity;
import com.pragma.foodcourtservice.infrastructure.output.jpa.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IOrderEntityMapper {
    OrderEntity toEntity(Order order);

    @Mapping(target = "order", ignore = true)
    OrderDishEntity toEntity(OrderDish orderDish);

    @Mapping(target = "pageNumber", source = "number")
    @Mapping(target = "pageSize", source = "size")
    @Mapping(target = "totalElements", source = "totalElements")
    @Mapping(target = "totalPages", source = "totalPages")
    @Mapping(target = "content", source = "content")
    PageModel<Order> toPageModel(Page<OrderEntity> pageResult);
}
