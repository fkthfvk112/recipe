package com.recipe.myrecipe.user.service;

import com.recipe.myrecipe.user.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    Long saveReview(ReviewDTO reviewDTO);
    List<ReviewDTO> getReviewsByRecipeId(Long recipeId);
}
