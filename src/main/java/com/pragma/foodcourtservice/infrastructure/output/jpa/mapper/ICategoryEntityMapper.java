package com.pragma.foodcourtservice.infrastructure.output.jpa.mapper;

import com.pragma.foodcourtservice.domain.model.Category;
import com.pragma.foodcourtservice.infrastructure.output.jpa.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ICategoryEntityMapper {
    Category toCategory(CategoryEntity categoryEntity);
}
