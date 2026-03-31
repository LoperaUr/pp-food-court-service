package com.pragma.foodcourtservice.infraestructure.output.jpa.repository;

import com.pragma.foodcourtservice.infraestructure.output.jpa.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
}
