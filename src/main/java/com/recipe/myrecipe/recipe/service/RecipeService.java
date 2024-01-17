package com.recipe.myrecipe.recipe.service;

import com.recipe.myrecipe.recipe.dto.GetDetailRecipeDTO;
import com.recipe.myrecipe.recipe.dto.RecipeDTO;

public interface RecipeService {
    boolean saveRecipe(RecipeDTO recipeDTO);
    RecipeDTO getRecipeById(Long recipeId);
    GetDetailRecipeDTO getDetailRecipeById(Long recipeId);
}
