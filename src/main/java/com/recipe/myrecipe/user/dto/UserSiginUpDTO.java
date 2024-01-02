package com.recipe.myrecipe.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UserSiginUpDTO {
    private String userId;
    private String userPassword;
    private String email;
    private String grantType;
}
