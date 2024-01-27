package com.recipe.myrecipe.user.service;

import com.recipe.myrecipe.common.dto.ResponseState;
import com.recipe.myrecipe.recipe.dto.RecipeIdNamePhotoDTO;
import com.recipe.myrecipe.user.dto.ToggleState;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LikeService {
    ResponseState toggleLike(String userId, int recipeId);
    ToggleState getToggleState(String userId, int recipeId);
    List<RecipeIdNamePhotoDTO> getLikeRecipes(String userId);
}
