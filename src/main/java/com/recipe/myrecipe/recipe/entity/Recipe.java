package com.recipe.myrecipe.recipe.entity;

import com.recipe.myrecipe.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="recipe")
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="recipe_id")
    private List<Ingredient> ingredients;

    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="recipe_id")
    private List<Step> steps;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id", unique = false)
    private User user;
}