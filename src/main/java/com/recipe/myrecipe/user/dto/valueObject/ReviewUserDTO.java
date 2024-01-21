package com.recipe.myrecipe.user.dto.valueObject;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewUserDTO {
    private Long userID;
    private String userNickName;
    private String userUrl;
}
