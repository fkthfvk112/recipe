package com.recipe.myrecipe.recipe.dto;

import com.recipe.myrecipe.recipe.dto.valueObject.RecipeOwnerInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class GetDetailRecipeDTO {
    @NotNull
    RecipeDTO recipeDTO;

    @NotNull RecipeOwnerInfo recipeOwnerInfo;
}
