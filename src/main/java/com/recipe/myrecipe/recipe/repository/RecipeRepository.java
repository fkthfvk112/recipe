package com.recipe.myrecipe.recipe.repository;

import com.recipe.myrecipe.recipe.entity.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

import java.util.Optional;


@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByRecipeName(String recipeName);

    Page<Recipe> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Recipe> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
