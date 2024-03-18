package com.recipe.myrecipe.recipe.dto.valueObject;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServingCondition {
    @NotNull
    @Size(min = 1, max = 20)
    private Integer min;

    @NotNull
    @Size(min = 1, max = 20)
    private Integer max;

    public ServingCondition(Integer min, Integer max){
        if(min > max){
            throw new IllegalArgumentException("min > max");
        }
        this.max = max;
        this.min = min;
    }
}
