package com.recipe.myrecipe.user.controller;

import com.recipe.myrecipe.common.dto.ResponseState;
import com.recipe.myrecipe.error.BusinessException;
import com.recipe.myrecipe.error.ErrorCode;
import com.recipe.myrecipe.recipe.dto.RecipeIdNamePhotoDTO;
import com.recipe.myrecipe.user.dto.ToggleState;
import com.recipe.myrecipe.user.dto.UserFeedInfo;
import com.recipe.myrecipe.user.dto.valueObject.RecipeIdWrapper;
import com.recipe.myrecipe.user.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/like")
public class LikeController {

    private LikeService likeService;

    @Autowired
    LikeController(LikeService likeService){
        this.likeService = likeService;
    }

    @PostMapping("/recipe/toggle")
    public ResponseEntity<String> toggleRecipeLike(@RequestBody RecipeIdWrapper recipeId){
        log.info("[toggleRecipeLike] - start with " + recipeId.getRecipeId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);

        likeService.toggleLike(authentication.getName(), recipeId.getRecipeId());

        return ResponseEntity.ok("토글 완료!");
    }

    @GetMapping("/recipe/like-state")
    public ResponseEntity<ToggleState> getToggleState(@RequestParam int recipeId){
        log.info("[getToggleState] - start with" + recipeId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);

        return ResponseEntity.ok(likeService.getToggleState(authentication.getName(), recipeId));
    }

    @GetMapping("/recipe/like-list")//프론트 구현
    public  ResponseEntity<List<RecipeIdNamePhotoDTO>> getMyLikeRecipes(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);

        return ResponseEntity.ok(likeService.getLikeRecipes(authentication.getName()));
    }
}
