package com.recipe.myrecipe.recipe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recipe.myrecipe.recipe.dto.RecipeDTO;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

	@PostMapping("/save-recipe")
	public ResponseEntity<String> saveRecipe(@RequestBody RecipeDTO recipeDTO) {
		
		
		return ResponseEntity.ok("success:save recipe");
	}
}
