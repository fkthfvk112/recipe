package com.recipe.myrecipe.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
public class UserLoginDTO {
    private String userId;
    private String userPassword;
    private String grantType;
    private String role;
}
