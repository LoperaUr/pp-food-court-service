package com.pragma.foodcourtservice.application.mapper;

import com.pragma.foodcourtservice.application.dto.OrderDTO;
import com.pragma.foodcourtservice.application.dto.PageResponseDTO;
import com.pragma.foodcourtservice.application.dto.TraceabilityDTO;
import com.pragma.foodcourtservice.domain.model.Order;
import com.pragma.foodcourtservice.domain.model.PageModel;
import com.pragma.foodcourtservice.domain.model.Traceability;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IOrderMapper {

    Order toModel(OrderDTO orderDTO);

    PageResponseDTO<OrderDTO> toPageResponseDTO(PageModel<Order> orders);

    List<TraceabilityDTO> toTraceabilityDTOList(List<Traceability> traceability);
}
