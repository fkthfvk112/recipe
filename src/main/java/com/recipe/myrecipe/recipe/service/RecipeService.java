package com.recipe.myrecipe.recipe.service;

import com.recipe.myrecipe.recipe.dto.GetDetailRecipeDTO;
import com.recipe.myrecipe.recipe.dto.RecipeDTO;
import com.recipe.myrecipe.recipe.dto.RecipeIdNamePhotoDTO;

import java.io.IOException;
import java.util.List;

public interface RecipeService {
    boolean saveRecipe(RecipeDTO recipeDTO);
    RecipeDTO getRecipeById(Long recipeId);
    GetDetailRecipeDTO getDetailRecipeById(Long recipeId);
    List<String> saveImageListToAPIserver(List<String> imgs)  throws IOException;
    String saveImageToAPIserver(String img) throws IOException;
    List<RecipeDTO> getRecentUsers(int page, int size);

    boolean updateRecipeViews(Long recipeId);

    List<RecipeIdNamePhotoDTO> getMyRecipeInfos(String userId, int page, int size);
}
