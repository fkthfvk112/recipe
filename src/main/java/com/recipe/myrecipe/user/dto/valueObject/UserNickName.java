package com.recipe.myrecipe.user.dto.valueObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserNickName {
    String nickName;
}
