package com.pragma.foodcourtservice.infrastructure.output.jpa.repository;

import com.pragma.foodcourtservice.infrastructure.output.jpa.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
