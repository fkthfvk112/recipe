package com.recipe.myrecipe.recipe.intergration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.myrecipe.MyrecipeApplication;
import com.recipe.myrecipe.recipe.dto.IngredientDTO;
import com.recipe.myrecipe.recipe.dto.RecipeDTO;
import com.recipe.myrecipe.recipe.dto.StepDTO;
import com.recipe.myrecipe.recipe.entity.Recipe;
import com.recipe.myrecipe.recipe.repository.RecipeRepository;
import com.recipe.myrecipe.user.dto.UserLoginDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MyrecipeApplication.class)
@Transactional
@AutoConfigureMockMvc
public class RecipeTest {
    @Autowired
    JdbcTemplate jdbc;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RecipeRepository recipeRepository;


    @Test
    void When_createRecipe_Expect_sotreIt() throws Exception {
        IngredientDTO ingre1 = IngredientDTO.builder()
                .name("test1")
                .qqt("test")
                .order(0).build();

        IngredientDTO ingre2 = IngredientDTO.builder()
                .name("test2")
                .qqt("test")
                .order(1).build();

        IngredientDTO ingre3 = IngredientDTO.builder()
                .name("test3")
                .qqt("test")
                .order(2).build();

        StepDTO step1 = StepDTO.builder()
                .description("hello test1")
                .order(1)
                .time(3)
                .build();

        StepDTO step2 = StepDTO.builder()
                .description("hello test2")
                .order(2)
                .time(30)
                .build();

        StepDTO step3 = StepDTO.builder()
                .description("hello test3")
                .order(3)
                .build();

        RecipeDTO dto = RecipeDTO.builder()
                .recipeName("test1")
                .categorie("test2")
                .servings(3)
                .cookMethod("test3")
                .ingredients(
                        List.of(ingre1, ingre2, ingre3)
                )
                .steps(
                        List.of(step1, step2, step3)
                )
                .description("testdesctestdesc")
                .build();

        MvcResult result = mockMvc.perform(post("/recipe/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        Recipe savedRecipe = recipeRepository.findByRecipeName("test1").get();

        Assertions.assertTrue(savedRecipe.getCategorie().equals(dto.getCategorie()), "저장 결과가 일치하지 않습니다.");
        Assertions.assertTrue(savedRecipe.getServings() == dto.getServings(), "저장 결과가 일치하지 않습니다.");
        Assertions.assertTrue(savedRecipe.getCookMethod().equals(dto.getCookMethod()), "저장 결과가 일치하지 않습니다.");
    }

    @Test
    void When_createInputIsIncorrect_Expect_exception() throws Exception {
        RecipeDTO dto = RecipeDTO.builder()
                .recipeName("")
                .categorie("")
                .servings(-1)
                .cookMethod("")
                .ingredients(List.of())
                .build();


        MvcResult result = mockMvc.perform(post("/recipe/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                        .andExpect(status().is4xxClientError()) //익셉션 핸들러에 등록하기!
                        .andExpect(jsonPath("$.status", is(400)))
                        .andExpect(jsonPath("$.code", is("C001")))
                        .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println(responseBody);

    }
}
