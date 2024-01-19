package com.recipe.myrecipe.recipe.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RecipeDTO {
    Long recipeId;

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
    List<String> repriPhotos;

    @NotEmpty
    @Size(min = 1)
    List<IngredientDTO> ingredients;

    @Size(min = 8, message = "최소값은 8")
    String description;

    List<StepDTO> steps;

    int views;
    private LocalDateTime createdAt;

}
