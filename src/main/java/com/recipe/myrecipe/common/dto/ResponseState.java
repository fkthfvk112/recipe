package com.recipe.myrecipe.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ResponseState {
    private String message;
    private boolean isSuccess;
}
