package com.recipe.myrecipe.recipe.service.impl;

import com.recipe.myrecipe.auth.util.UserUtil;
import com.recipe.myrecipe.recipe.dto.GetDetailRecipeDTO;
import com.recipe.myrecipe.recipe.dto.IngredientDTO;
import com.recipe.myrecipe.recipe.dto.RecipeDTO;
import com.recipe.myrecipe.recipe.dto.StepDTO;
import com.recipe.myrecipe.recipe.dto.valueObject.RecipeOwnerInfo;
import com.recipe.myrecipe.recipe.entity.Ingredient;
import com.recipe.myrecipe.recipe.entity.Recipe;
import com.recipe.myrecipe.recipe.entity.Step;
import com.recipe.myrecipe.recipe.repository.RecipeRepository;
import com.recipe.myrecipe.recipe.service.RecipeService;
import com.recipe.myrecipe.user.entity.User;
import com.recipe.myrecipe.user.repository.UserRepository;
import com.recipe.myrecipe.util.DtoToEntity;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private ModelMapper modelMapper;
    private UserRepository userRepository;
    private RecipeRepository recipeRepository;
    private DtoToEntity dtoToEntity;
    private UserUtil userUtil;

    @Autowired
    RecipeServiceImpl(ModelMapper modelMapper, RecipeRepository recipeRepository,
                      DtoToEntity dtoToEntity, UserUtil userUtil,
                      UserRepository userRepository){
        this.modelMapper = modelMapper;
        this.recipeRepository = recipeRepository;
        this.dtoToEntity = dtoToEntity;
        this.userUtil = userUtil;
        this.userRepository = userRepository;
    }

    public boolean saveRecipe(RecipeDTO recipeDTO){
        try{
            String userId = userUtil.getUserId();
            Recipe recipe = dtoToEntity.RecipeDtoToRecipeEntity(recipeDTO);

            log.info("[saveRecipe] recipe : {}", recipe);

            User uploadUser = userRepository.getByUserId(userId).get();

            recipe.setUser(uploadUser);
            recipeRepository.save(recipe);
            return true;
        } catch (Exception e){
            log.info("[RecipeServiceImpl/saveRecipe] - save fail {}", e);
            return false;
        }
    }

    public RecipeDTO getRecipeById(Long recipeId){
        try{
            log.info("[getRecipeById] - start");
            return dtoToEntity.RecipeEntityToRecipeDTO(recipeRepository.findById(recipeId).get());
        }
        catch (Exception e){
            log.info("[getRecipeById] - error : {}", e);
            return null;
        }
    }

    public GetDetailRecipeDTO getDetailRecipeById(Long recipeId){
        try{
            log.info("[getDetailRecipeById] - start with Id : {}", recipeId);

            Recipe recipe = recipeRepository.findById(recipeId).get();
            User recipeOwner = recipe.getUser();
            //수정 - 추가 : 유저 관련 작업
            log.info("[getDetailRecipeById] - recipe : {}", recipe);

            RecipeDTO recipeDTO = dtoToEntity.RecipeEntityToRecipeDTO(recipe);
            RecipeOwnerInfo recipeOwnerInfo = RecipeOwnerInfo.builder()
                    .userId(recipeOwner.getUserId())
                    .build();

            log.info("[getDetailRecipeById] - dto : {}", recipeDTO);

            return GetDetailRecipeDTO.builder()
                    .recipeDTO(recipeDTO)
                    .recipeOwnerInfo(recipeOwnerInfo)
                    .build();
        }catch (Exception e){
            log.info("[getDetailRecipeById] - error : {}", e);
            return null;
        }
    }
}
