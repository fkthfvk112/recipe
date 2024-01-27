package com.recipe.myrecipe.user.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ToggleState {
    public boolean isOn;
}
