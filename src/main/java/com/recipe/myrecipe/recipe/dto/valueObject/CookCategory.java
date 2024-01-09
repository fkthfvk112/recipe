package com.recipe.myrecipe.recipe.dto.valueObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CookCategory {
	String sort;//음료, 디저트, 면 등
	String condition;//아플 떄 등
	String technique;//굽기, 튀기기 등
}
