package com.recipe.myrecipe.recipe.entity;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Builder
@Table(name="step")
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int stepOrder;
    private String photo;
    private String description;
    private int time;
}
