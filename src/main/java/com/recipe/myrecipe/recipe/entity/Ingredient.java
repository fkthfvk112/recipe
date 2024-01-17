package com.recipe.myrecipe.recipe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="ingredient")
@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String name;
    String qqt;
    int ingreOrder;

}
