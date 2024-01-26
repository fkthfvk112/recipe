package com.recipe.myrecipe.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UpdateFeedInfo {
    String nickName;

    @Email
    String userUrl;
    String userPhoto;
}
