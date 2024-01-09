package com.recipe.myrecipe.recipe.dto.valueObject;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IngredientInfo {
	private String ingreName;
	private String ingreQuantity;
}
