package com.recipe.myrecipe.user.dto.valueObject;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecipeIdWrapper {
    private int recipeId;
}
