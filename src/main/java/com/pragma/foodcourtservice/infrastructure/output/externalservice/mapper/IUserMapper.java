package com.pragma.foodcourtservice.infrastructure.output.externalservice.mapper;

import com.pragma.foodcourtservice.domain.model.User;
import com.pragma.foodcourtservice.infrastructure.output.externalservice.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IUserMapper {
    User toModel(UserDTO dto);
}
