package com.recipe.myrecipe.util;

import com.recipe.myrecipe.recipe.dto.IngredientDTO;
import com.recipe.myrecipe.recipe.dto.RecipeDTO;
import com.recipe.myrecipe.recipe.dto.StepDTO;
import com.recipe.myrecipe.recipe.entity.Ingredient;
import com.recipe.myrecipe.recipe.entity.Recipe;
import com.recipe.myrecipe.recipe.entity.Step;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

public class DtoToEntity {
    public Recipe RecipeDtoToRecipeEntity(RecipeDTO recipeDTO){
        List<Ingredient> ingredients = new ArrayList<>();
        List<Step> steps = new ArrayList<>();

        List<IngredientDTO> ingredientDTOS = recipeDTO.getIngredients();
        List<StepDTO> stepDTOS = recipeDTO.getSteps();

        for(IngredientDTO dto : ingredientDTOS){
            Ingredient ingredient = Ingredient.builder()
                    .qqt(dto.getQqt())
                    .name(dto.getName())
                    .ingreOrder(dto.getOrder())
                    .build();
            ingredients.add(ingredient);
        }

        for(StepDTO dto : stepDTOS){
            Step step = Step.builder()
                    .photo("") // 수정...저장된 사진 url
                    .time(dto.getTime())
                    .stepOrder(dto.getOrder())
                    .description(dto.getDescription()).build();
            steps.add(step);
        }

        return Recipe.builder()
                .recipeName(recipeDTO.getRecipeName())
                .categorie(recipeDTO.getCategorie())
                .cookMethod(recipeDTO.getCookMethod())
                .servings(recipeDTO.getServings())
                .description(recipeDTO.getDescription())
                .ingredients(ingredients)
                .steps(steps)
                .build();

    }
}
