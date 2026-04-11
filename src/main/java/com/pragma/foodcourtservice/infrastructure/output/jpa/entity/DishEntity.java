package com.pragma.foodcourtservice.infrastructure.output.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "dishes")
@AllArgsConstructor
@NoArgsConstructor
public class DishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private Long restaurantId;

    @Column(nullable = false)
    private String urlImage;

    private boolean active;

    @Column(nullable = false)
    private Long price;

}
