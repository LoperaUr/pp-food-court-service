package com.pragma.foodcourtservice.application.mapper;

import com.pragma.foodcourtservice.application.dto.OrderDTO;
import com.pragma.foodcourtservice.application.dto.PageResponseDTO;
import com.pragma.foodcourtservice.domain.model.Order;
import com.pragma.foodcourtservice.domain.model.PageModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IOrderMapper {

    Order toModel(OrderDTO orderDTO);

    PageResponseDTO<OrderDTO> toPageResponseDTO(PageModel<Order> orders);
}
