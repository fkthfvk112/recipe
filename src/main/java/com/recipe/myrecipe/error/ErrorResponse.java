package com.recipe.myrecipe.error;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
        private String message;
        private int status;
        private String error;
        private String code;
}
