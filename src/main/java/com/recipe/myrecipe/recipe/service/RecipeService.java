package com.recipe.myrecipe.recipe.service;

import com.recipe.myrecipe.recipe.dto.*;
import com.recipe.myrecipe.recipe.entity.Recipe;
import com.recipe.myrecipe.user.dto.valueObject.UserNickName;

import java.io.IOException;
import java.util.List;

public interface RecipeService {
    boolean saveRecipe(RecipeDTO recipeDTO);
    RecipeDTO getRecipeById(Long recipeId);
    GetDetailRecipeDTO getDetailRecipeById(Long recipeId);
    List<String> saveImageListToAPIserver(List<String> imgs)  throws IOException;
    String saveImageToAPIserver(String img) throws IOException;
    List<RecipeDTO> getRecentRecipes(int page, int size);

    boolean updateRecipeViews(Long recipeId);

    List<RecipeIdNamePhotoDTO> getMyRecipeInfos(String userId, int page, int size);
    List<RecipeIdNamePhotoDTO> getUserRecipeInfos(UserNickName userNickName, int page, int size);

    List<RecipeDTO> getRecipesByIngres(List<String> ingredients, int page, int size);

    List<RecipeDTO> getRecipesBySearchingCondtion(RecipeSearchingCondition searchingCon, RecipeSortingConEnum sortingCon, int page, int size);
}
