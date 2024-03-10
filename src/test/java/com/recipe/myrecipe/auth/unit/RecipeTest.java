package com.recipe.myrecipe.auth.unit;

import com.recipe.myrecipe.MyrecipeApplication;
import com.recipe.myrecipe.recipe.dto.IngredientDTO;
import com.recipe.myrecipe.recipe.dto.RecipeDTO;
import com.recipe.myrecipe.recipe.dto.RecipeSearchingCondition;
import com.recipe.myrecipe.recipe.dto.RecipeSortingConEnum;
import com.recipe.myrecipe.recipe.dto.valueObject.ServingCondition;
import com.recipe.myrecipe.recipe.entity.Ingredient;
import com.recipe.myrecipe.recipe.entity.Recipe;
import com.recipe.myrecipe.recipe.repository.RecipeRepository;
import com.recipe.myrecipe.recipe.service.RecipeService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;



//@Transactional
@SpringBootTest(classes = MyrecipeApplication.class)
public class RecipeTest {

    @Autowired
    RecipeService recipeService;

    @Test
    //@Sql("/TestQuery/TestInsertQuery.sql")
    void When_InputSearchingCondition_Expect_Recipe(){

        //                LocalDateTime.of(2024, 2, 16, 0, 0),
        //searching condition test one (recipe name - full name)
        RecipeSearchingCondition searchingCon = new RecipeSearchingCondition("테스트요리", null,
                null, null, null, null, false);

        List<RecipeDTO> resultList =  recipeService.getRecipesBySearchingCondition(searchingCon,
                RecipeSortingConEnum.valueOf("VIEW_MANY"), 1, 3);

        assertSame(resultList.size(), 3);
        assertTrue(resultList.get(0).getRecipeName().contains("테스트요리"));



        //searching condition test two (recipe name - few name(full text search)
        searchingCon = new RecipeSearchingCondition("테스트 요", null,
                null, null, null, null, false);

        resultList =  recipeService.getRecipesBySearchingCondition(searchingCon,
                RecipeSortingConEnum.valueOf("VIEW_MANY"), 1, 20);

        assertSame(resultList.size(), 20);
        assertEquals(resultList.get(0).getRecipeName(), "테스트요리20");



        //searching condition test three (recipe date after)
        searchingCon = new RecipeSearchingCondition(null, LocalDateTime.of(2024, 3, 10, 0, 0),
                null, null, null, null, false);

        resultList =  recipeService.getRecipesBySearchingCondition(searchingCon,
                RecipeSortingConEnum.valueOf("VIEW_MANY"), 1, 10);


        for(int i = 0; i < 10; i++){
            Random random = new Random();
            int randInx = random.nextInt(resultList.size());
            assertTrue(resultList.get(randInx).getCreatedAt()
                    .compareTo(LocalDateTime.of(2024, 3, 10, 0, 0)) > 0);
        }
        assertSame(resultList.size(), 10);



        //searching condition test four (recipe cook method)
        searchingCon = new RecipeSearchingCondition(null, null,
                "찌기", null, null, null, false);

        resultList =  recipeService.getRecipesBySearchingCondition(searchingCon,
                RecipeSortingConEnum.valueOf("VIEW_MANY"), 1, 10);

        for(int i = 0; i < 10; i++){
            Random random = new Random();
            int randInx = random.nextInt(resultList.size());

            assertEquals(resultList.get(randInx).getCookMethod(), "찌기");
        }



        //searching condition test five (recipe ingredient name - full name, or Condition)
        searchingCon = new RecipeSearchingCondition(null, null,
                 null, List.of("감자", "큰땡땡이"), null, null, false);

        resultList =  recipeService.getRecipesBySearchingCondition(searchingCon,
                RecipeSortingConEnum.valueOf("VIEW_MANY"), 1, 10);

        for(int i = 0; i < 10; i++){
            Random random = new Random();
            int randInx = random.nextInt(resultList.size());

            boolean isContainIngre = false;

            List<IngredientDTO> nowIngres = resultList.get(randInx).getIngredients();
            System.out.println("현제 " + nowIngres);
            for(IngredientDTO ingre : nowIngres){
                if(ingre.getName().contains("감자")){
                    isContainIngre = true;
                }
            }
            assertTrue(isContainIngre);
        }



        //searching condition test five (recipe ingredient name - full name, and Condition -1)
        searchingCon = new RecipeSearchingCondition(null, null,
                null, List.of("감자", "큰땡땡이"), null, null, true);

        resultList =  recipeService.getRecipesBySearchingCondition(searchingCon,
                RecipeSortingConEnum.valueOf("VIEW_MANY"), 1, 10);

        for(int i = 0; i < resultList.size(); i++){
            Random random = new Random();
            int randInx = random.nextInt(resultList.size());

            boolean isContainIngre = false;

            List<IngredientDTO> nowIngres = resultList.get(randInx).getIngredients();
            for(IngredientDTO ingre : nowIngres){
                if(ingre.getName().contains("감자")){
                    isContainIngre = true;
                }
            }
            assertFalse(isContainIngre);
        }



        //searching condition test five (recipe ingredient name - full name, and Condition -2)
        searchingCon = new RecipeSearchingCondition(null, null,
                null, List.of("감자", "양파"), null, null, true);

        resultList =  recipeService.getRecipesBySearchingCondition(searchingCon,
                RecipeSortingConEnum.valueOf("VIEW_MANY"), 1, 10);

        for(int i = 0; i < 10; i++){
            Random random = new Random();
            int randInx = random.nextInt(resultList.size());

            boolean isContainIngre = false;
            int ingreCnt = 0;

            List<IngredientDTO> nowIngres = resultList.get(randInx).getIngredients();
            for(IngredientDTO ingre : nowIngres){
                if(ingre.getName().contains("감자") || ingre.getName().contains("양파")){
                    ingreCnt ++;
                    if(ingreCnt == 2){
                        isContainIngre = true;
                    }
                }
            }
            assertTrue(isContainIngre);
        }


        //searching condition test five (recipe ingredient name - few name, and Condition)
        searchingCon = new RecipeSearchingCondition(null, null,
                null, List.of("감자", "양파"), null, null, true);

        resultList =  recipeService.getRecipesBySearchingCondition(searchingCon,
                RecipeSortingConEnum.valueOf("VIEW_MANY"), 1, 10);

        for(int i = 0; i < 10; i++){
            Random random = new Random();
            int randInx = random.nextInt(resultList.size());

            boolean isContainIngre = false;
            int ingreCnt = 0;

            List<IngredientDTO> nowIngres = resultList.get(randInx).getIngredients();
            for(IngredientDTO ingre : nowIngres){
                if(ingre.getName().contains("감자") || ingre.getName().contains("양파")){
                    ingreCnt ++;
                    if(ingreCnt == 2){
                        isContainIngre = true;
                    }
                }
            }
            assertTrue(isContainIngre);
        }


        //searching condition test six (recipe servings between)
        int minVal = 2;
        int maxVal = 3;
        ServingCondition servingCon = new ServingCondition(minVal, maxVal);

        searchingCon = new RecipeSearchingCondition(null, null,
                null, null, servingCon, null, false);

        resultList =  recipeService.getRecipesBySearchingCondition(searchingCon,
                RecipeSortingConEnum.valueOf("VIEW_MANY"), 1, 10);


        for(int i = 0; i < 10; i++){
            Random random = new Random();
            int randInx = random.nextInt(resultList.size());
            assertTrue(resultList.get(randInx).getServings() >= minVal && resultList.get(randInx).getServings() <= maxVal);
        }


        //whole test
        minVal = 2;
        maxVal = 2;
        servingCon = new ServingCondition(minVal, maxVal);

        searchingCon = new RecipeSearchingCondition(null, LocalDateTime.of(2024, 3, 10, 0, 0),
                "튀기기", List.of("치즈", "베이컨"), servingCon, "중식", true);

        resultList =  recipeService.getRecipesBySearchingCondition(searchingCon,
                RecipeSortingConEnum.valueOf("VIEW_MANY"), 1, 10);

        assertEquals(resultList.get(0).getRecipeName(), "테스트요리7");
    }
}
