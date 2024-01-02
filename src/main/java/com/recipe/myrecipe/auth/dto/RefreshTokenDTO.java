package com.recipe.myrecipe.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class RefreshTokenDTO {
    private String userName;
    private String refreshToken;
}
