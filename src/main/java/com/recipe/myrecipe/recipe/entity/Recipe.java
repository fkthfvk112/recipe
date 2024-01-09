package com.recipe.myrecipe.recipe.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name="recipe")
@ToString
@Getter
@Builder
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="recipe_id")
    private Long id;

    @Column(nullable = false)
    private String recipeName;
    private String categorie;
    private int servings;
    private String cookMethod;

    @OneToMany
    @JoinColumn(name="recipe_id")
    private List<Ingredient> ingredients;
    private String description;

    @OneToMany
    @JoinColumn(name="recipe_id")
    private List<Step> steps;
}