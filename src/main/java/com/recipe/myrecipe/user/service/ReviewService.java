package com.recipe.myrecipe.user.service;

import com.recipe.myrecipe.user.dto.ReviewDTO;
import com.recipe.myrecipe.user.dto.ReviewResultDTO;

import java.util.List;

public interface ReviewService {
    Long saveReview(ReviewDTO reviewDTO);
    List<ReviewResultDTO> getReviewsByRecipeId(Long recipeId);
}
