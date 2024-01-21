package com.recipe.myrecipe.recipe.controller;

import com.recipe.myrecipe.error.BusinessException;
import com.recipe.myrecipe.error.ErrorCode;
import com.recipe.myrecipe.recipe.dto.GetDetailRecipeDTO;
import com.recipe.myrecipe.recipe.dto.RecipeDTO;
import com.recipe.myrecipe.recipe.dto.RecipeIdNamePhotoDTO;
import com.recipe.myrecipe.recipe.service.RecipeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.DriverManager;
import java.util.List;

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
       log.info("[createRecipe] - start with dto : {} ",recipe);

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
            recipeService.updateRecipeViews(recipeId);
            return ResponseEntity.ok(getDetailRecipeDTO);
        } else{
            throw new IllegalArgumentException();
        }
    }

    @GetMapping("/get-new-recipe")
    public ResponseEntity<List<RecipeDTO>> getRecentRecipes( @RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "10") int size){
        List<RecipeDTO> recipes = recipeService.getRecentUsers(page, size);

        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/get-my-recipe")
    public ResponseEntity<List<RecipeIdNamePhotoDTO>> getMyRecipes(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                   @RequestParam(value = "size", defaultValue = "10") int size){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            log.info("[saveReview] - Error : auth is null");
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }

        String userId = authentication.getName();

        return ResponseEntity.ok(recipeService.getMyRecipeInfos(userId, page, size));
    }
}
