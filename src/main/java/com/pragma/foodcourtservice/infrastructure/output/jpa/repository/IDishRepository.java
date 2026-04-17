package com.pragma.foodcourtservice.infrastructure.output.jpa.repository;

import com.pragma.foodcourtservice.infrastructure.output.jpa.entity.DishEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {
    Page<DishEntity> findByRestaurantIdAndActiveIsTrue(Long restaurantId, Pageable pageable);

    Page<DishEntity> findByRestaurantIdAndCategoryIdAndActiveIsTrue(Long id, Long categoryId, Pageable pageable);
}
