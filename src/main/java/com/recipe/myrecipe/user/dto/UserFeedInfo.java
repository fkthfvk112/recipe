package com.recipe.myrecipe.user.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SecondaryRow;

@Builder
@Setter
@Getter
public class UserFeedInfo {
    private String userId;
    private String nickName;
    private String email;
    private String grantType;
    private String userPhoto;
    private String userUrl;
}
