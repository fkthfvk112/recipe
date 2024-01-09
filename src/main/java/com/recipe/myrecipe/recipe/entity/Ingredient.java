package com.recipe.myrecipe.recipe.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.ToString;

@Entity
@Table(name="ingredient")
@ToString
@Builder
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String name;
    String qqt;
    int ingreOrder;

}
