package com.recipe.myrecipe.recipe.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StepDTO {
    int order;
    MultipartFile photo;

    String photoUrl;
    @Size(min = 5)
    String description;
    int time;
}
