package com.recipe.myrecipe.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class TokenDTO {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private List<String> autorities;
}
