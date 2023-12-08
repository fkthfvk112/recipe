package com.recipe.myrecipe.recipe.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.recipe.myrecipe.recipe.dao.RecipeDao;
import com.recipe.myrecipe.recipe.dto.RecipeDTO;
import com.recipe.myrecipe.recipe.service.RecipeService;

public class RecipeServiceImpl implements RecipeService {
 
	RecipeDao recipeDao;
	
	@Autowired
	RecipeServiceImpl(RecipeDao recipeDao){
		this.recipeDao = recipeDao;
	}

	@Override
	public Long saveRecipe(RecipeDTO recipeDTO) {
		try {
			return recipeDao.saveRecipe(recipeDTO);
		}catch(Exception e) {
	        throw new RuntimeException("Failed to save recipe", e);//수정
		}
	}
}
