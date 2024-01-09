package com.recipe.myrecipe.recipe.controller;

import com.recipe.myrecipe.recipe.dto.RecipeDTO;
import com.recipe.myrecipe.recipe.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/recipe")
@Controller
public class RecipeController {

    RecipeService recipeService;

    @Autowired
    RecipeController(RecipeService recipeService){
        this.recipeService = recipeService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createRecipe(@Valid @RequestBody RecipeDTO recipe){
        System.out.println(recipe);
        if(recipeService.saveRecipe(recipe)){
            return ResponseEntity.ok("saved");
        }
        else{
            throw new IllegalArgumentException();
        }
    }
}
