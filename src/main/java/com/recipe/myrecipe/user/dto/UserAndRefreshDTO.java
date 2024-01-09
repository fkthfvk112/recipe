package com.recipe.myrecipe.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class UserAndRefreshDTO {
    String userId;
    String refreshToken;
    List<String> roles;
}
