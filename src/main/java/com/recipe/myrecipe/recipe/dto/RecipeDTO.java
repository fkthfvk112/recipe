package com.recipe.myrecipe.recipe.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecipeDTO {
    @NotBlank
    @Size(min = 1, message = "최소값은 3")
    @Size(max = 20, message = "최대값은 20")
    String recipeName;
    @NotBlank
    String categorie;

    @Min(value = 1, message = "최소값은 1.")
    @Max(value = 6, message = "최대값은 5.")
    int servings;

    @NotBlank
    String cookMethod;

    @NotEmpty
    @Size(min = 1)
    List<IngredientDTO> ingredients;

    @Size(min = 8, message = "최소값은 8")
    String description;

    List<StepDTO> steps;

}
