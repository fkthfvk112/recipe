package com.recipe.myrecipe.recipe.dto;

import com.recipe.myrecipe.recipe.dto.valueObject.ServingCondition;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RecipeSearchingCondition {

    @Nullable
    private String recipeName = null;
    //n월 이후 ex)최근 6개월
    @Nullable
    private LocalDateTime createdDate = null;
    @Nullable
    private String cookMethod = null;
    @Nullable
    private List<String> ingredientNames = null;
    @Nullable
    private Boolean ingredientAndCon = null;//재료로 검색시 false일경우 or조건 true이면 and조건
    @Nullable
    private ServingCondition servingsCon = null;
    @Nullable
    private String cookCategory = null;
    public RecipeSearchingCondition(String recipeName, LocalDateTime createdDate, String cookMethod,
                                    List<String> ingredientNames, ServingCondition servingsCon, String cookCategory, boolean ingredientAndCon){
        if (recipeName == null && createdDate == null && cookMethod == null &&
                ingredientNames == null && servingsCon == null && cookCategory == null) {
            throw new IllegalArgumentException("Invalid searching condition: every thing is null");
        }

        this.recipeName = recipeName;
        this.createdDate = createdDate;
        this.cookMethod = cookMethod;
        this.ingredientNames = ingredientNames;
        this.servingsCon = servingsCon;
        this.cookCategory = cookCategory;
        this.ingredientAndCon = ingredientAndCon;
    }
}
