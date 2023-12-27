package com.recipe.myrecipe.user.dto;

import lombok.Builder;

import java.util.List;

@Builder
public class SignInResultDTO {
    private String refreshToken;
    private String accessToken;
    private List<String> roles;
}
