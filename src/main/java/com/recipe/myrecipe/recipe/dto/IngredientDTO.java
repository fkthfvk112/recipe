package com.recipe.myrecipe.recipe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IngredientDTO {

    @NotBlank
    @Size(max = 100)
    String name;

    @NotBlank
    @Size(max = 100)
    String qqt;
    int order;
}
