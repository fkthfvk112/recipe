package com.recipe.myrecipe.user.dto;

import com.recipe.myrecipe.user.dto.valueObject.ReviewUserDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewResultDTO {
    int score;

    String message;

    private Long recipeId;

    private LocalDateTime createdAt;

    private ReviewUserDTO userInfo;
}
