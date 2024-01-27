package com.recipe.myrecipe.user.repository;

import com.recipe.myrecipe.user.entity.LikeRecipe;
import com.recipe.myrecipe.user.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeRecipe, Long> {

    Optional<LikeRecipe> findByUserUserIdAndRecipeId(String userId, Long recipeId);

    List<LikeRecipe> findByUserUserId(String userId);

}
