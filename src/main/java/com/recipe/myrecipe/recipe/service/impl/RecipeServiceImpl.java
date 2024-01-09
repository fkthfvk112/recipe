package com.recipe.myrecipe.recipe.service.impl;

import com.recipe.myrecipe.recipe.dto.IngredientDTO;
import com.recipe.myrecipe.recipe.dto.RecipeDTO;
import com.recipe.myrecipe.recipe.dto.StepDTO;
import com.recipe.myrecipe.recipe.entity.Ingredient;
import com.recipe.myrecipe.recipe.entity.Recipe;
import com.recipe.myrecipe.recipe.entity.Step;
import com.recipe.myrecipe.recipe.repository.RecipeRepository;
import com.recipe.myrecipe.recipe.service.RecipeService;
import com.recipe.myrecipe.util.DtoToEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {

    private ModelMapper modelMapper;
    private RecipeRepository recipeRepository;
    private DtoToEntity dtoToEntity;

    @Autowired
    RecipeServiceImpl(ModelMapper modelMapper, RecipeRepository recipeRepository, DtoToEntity dtoToEntity){
        this.modelMapper = modelMapper;
        this.recipeRepository = recipeRepository;
        this.dtoToEntity = dtoToEntity;
    }

    public boolean saveRecipe(RecipeDTO recipeDTO){
        try{
            Recipe recipe = dtoToEntity.RecipeDtoToRecipeEntity(recipeDTO);
            recipeRepository.save(recipe);
            return true;
        } catch (IllegalArgumentException e){
            return false;
        }
    }
}
