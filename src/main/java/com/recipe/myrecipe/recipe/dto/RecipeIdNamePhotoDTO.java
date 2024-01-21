package com.recipe.myrecipe.recipe.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RecipeIdNamePhotoDTO {
    Long recipeId;

    String recipeName;

    List<String> repriPhotos;
}
