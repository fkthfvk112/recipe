package com.recipe.myrecipe.recipe.dao;

import com.recipe.myrecipe.recipe.dto.RecipeDTO;

public interface RecipeDao {
	Long saveRecipe(RecipeDTO recipeDTO);
}
