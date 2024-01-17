package com.recipe.myrecipe.recipe.controller;

import com.recipe.myrecipe.recipe.dto.GetDetailRecipeDTO;
import com.recipe.myrecipe.recipe.dto.RecipeDTO;
import com.recipe.myrecipe.recipe.service.RecipeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.DriverManager;

@Slf4j
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

    @GetMapping("/get-recipe")
    public ResponseEntity<GetDetailRecipeDTO> getRecipeDetail(@RequestParam Long recipeId){
        log.info("[getRecipeDetail] - start with id : {}", recipeId);
        GetDetailRecipeDTO getDetailRecipeDTO = recipeService.getDetailRecipeById(recipeId);

        if(getDetailRecipeDTO != null){
            return ResponseEntity.ok(getDetailRecipeDTO);
        } else{
            throw new IllegalArgumentException();
        }
    }
}
