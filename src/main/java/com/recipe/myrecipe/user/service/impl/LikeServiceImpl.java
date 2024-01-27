package com.recipe.myrecipe.user.service.impl;

import com.recipe.myrecipe.common.dto.ResponseState;
import com.recipe.myrecipe.recipe.dto.RecipeIdNamePhotoDTO;
import com.recipe.myrecipe.recipe.repository.RecipeRepository;
import com.recipe.myrecipe.user.dto.ToggleState;
import com.recipe.myrecipe.user.entity.LikeRecipe;
import com.recipe.myrecipe.user.repository.LikeRepository;
import com.recipe.myrecipe.user.repository.UserRepository;
import com.recipe.myrecipe.user.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {

    private LikeRepository likeRepository;
    private UserRepository userRepository;
    private RecipeRepository recipeRepository;

    @Autowired
    LikeServiceImpl(LikeRepository likeRepository, UserRepository userRepository, RecipeRepository recipeRepository){
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }
    @Override
    public ResponseState toggleLike(String userId, int recipeId) {
        Long recipeLongId = Long.valueOf(recipeId);

        Optional<LikeRecipe> optionalLikeRecipe =  likeRepository.findByUserUserIdAndRecipeId(userId, recipeLongId);

        if(optionalLikeRecipe.isPresent()){
            likeRepository.delete(optionalLikeRecipe.get());
        }else{
            LikeRecipe likeRecipe = LikeRecipe.builder()
                    .recipe(recipeRepository.findById(recipeLongId).get())
                    .user(userRepository.findByUserId(userId).get())
                    .build();

            likeRepository.save(likeRecipe);
        }

        return ResponseState.builder()
                .isSuccess(true)
                .message("라이크 토글링 완료")
                .build();
    }

    @Override
    public ToggleState getToggleState(String userId, int recipeId) {
        Optional<LikeRecipe> optionalLikeRecipe =  likeRepository.findByUserUserIdAndRecipeId(userId, Long.valueOf(recipeId));

        if(optionalLikeRecipe.isPresent()){
            return ToggleState.builder()
                    .isOn(true)
                    .build();
        }else{
            return ToggleState.builder()
                    .isOn(false)
                    .build();
        }
    }

    @Override
    public List<RecipeIdNamePhotoDTO> getLikeRecipes(String userId) {
        List<LikeRecipe> likeRecipeList = likeRepository.findByUserUserId(userId);


        return likeRecipeList.stream().map((ele)->
            RecipeIdNamePhotoDTO.builder()
                    .repriPhotos(ele.getRecipe().getRepriPhotos())
                    .recipeId(ele.getRecipe().getId())
                    .recipeName(ele.getRecipe().getRecipeName())
                    .build()
        ).collect(Collectors.toList());
    }
}
