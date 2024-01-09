package com.recipe.myrecipe.user.dto;

import lombok.*;

import java.util.List;

@Builder
@ToString
@Getter
public class SignInResultDTO {
    private String refreshToken;
    private String accessToken;
    private boolean isSuccess;
    private List<String> roles;
}
