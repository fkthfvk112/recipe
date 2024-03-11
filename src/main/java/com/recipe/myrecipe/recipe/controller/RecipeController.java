package com.recipe.myrecipe.recipe.controller;

import com.recipe.myrecipe.error.BusinessException;
import com.recipe.myrecipe.error.ErrorCode;
import com.recipe.myrecipe.recipe.dto.*;
import com.recipe.myrecipe.recipe.service.RecipeService;
import com.recipe.myrecipe.user.dto.valueObject.UserNickName;
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

    @GetMapping("/recent-recipe")
    public ResponseEntity<List<RecipeDTO>> getRecentRecipes( @RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "10") int size){
        List<RecipeDTO> recipes = recipeService.getRecentRecipes(page, size);

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

    @GetMapping("/get-user-recipe")
    public ResponseEntity<List<RecipeIdNamePhotoDTO>> getUserRecipes(
            @RequestParam String userNickName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){

        log.info("[getUserRecipes] - start with " + userNickName);

        return ResponseEntity.ok(recipeService.getUserRecipeInfos(new UserNickName(userNickName), page, size));
    }

    @GetMapping("/ingres")
    public ResponseEntity<List<RecipeDTO>> getRecipesByIngres(@RequestParam(value = "ingres") List<String> ingredients,
                                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                                              @RequestParam(value = "size", defaultValue = "10") int size){

        log.info("[getRecipesByIngres] - start with " + ingredients.toString());

        List<RecipeDTO> recipes = recipeService.getRecipesByIngres(ingredients,page, size);

        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/conditions")
    public ResponseEntity<List<RecipeDTO>> getRecipesByConditions(@RequestParam(value = "searchingCondition")RecipeSearchingCondition searchingCon,
                                                                  @RequestParam(value = "sortingCondition")RecipeSortingConEnum sortingCon,
                                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                                  @RequestParam(value = "size", defaultValue = "10") int size){
        log.info("[getRecipesByConditions] - start with condition : " + sortingCon + "\n sorting condtion " + sortingCon);

        return ResponseEntity.ok(recipeService.getRecipesBySearchingCondition(searchingCon, sortingCon, page, size));
    }
}
