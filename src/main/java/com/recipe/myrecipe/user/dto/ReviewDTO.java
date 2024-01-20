package com.recipe.myrecipe.user.dto;

import com.recipe.myrecipe.recipe.entity.Recipe;
import com.recipe.myrecipe.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewDTO {
    @Min(value = 1, message = "Score must be at least 1")
    @Max(value = 5, message = "Score must be at most 5")
    int score;

    @Size(min = 3, max = 50, message = "Message length must be between 3 and 50 characters")
    String message;

    @NotNull
    private Long recipeId;

    private LocalDateTime createdAt;
}
