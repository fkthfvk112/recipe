package com.recipe.myrecipe.user.repository;

import com.recipe.myrecipe.user.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<List<Review>> findByRecipeId(Long recipeId);
}
