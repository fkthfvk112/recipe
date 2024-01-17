package com.recipe.myrecipe.recipe.dto.valueObject;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Setter
@Getter
public class RecipeOwnerInfo {

    @NotBlank
    String userId;

    //유저 url 등...
}
