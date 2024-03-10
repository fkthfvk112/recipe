package com.recipe.myrecipe.recipe.repository;

import com.recipe.myrecipe.recipe.dto.RecipeSearchingCondition;
import com.recipe.myrecipe.recipe.dto.RecipeSortingConEnum;
import com.recipe.myrecipe.recipe.entity.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByRecipeName(String recipeName);

    Page<Recipe> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Recipe> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.ingredients i WHERE i.name IN :ingres")
    Page<Recipe> findAllByIngredients(List<String> ingres, Pageable pageable);

    @Procedure("sp_searchingRecipeByConditionsAndSortByConditions")
    Optional<List<Recipe>> findRecipeByConditions(String recipeName, LocalDateTime createdDate, String cookMethod, Boolean ingredientAndCon,
                                  String ingredientDelimString, Integer ServingConditionMin, Integer ServingConditionMax, String cookCategory,
                                  String RecipeSortingCon, Integer pageVal, Integer sizeVal);
}
