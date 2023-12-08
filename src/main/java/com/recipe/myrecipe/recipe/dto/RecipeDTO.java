package com.recipe.myrecipe.recipe.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.recipe.myrecipe.recipe.dto.valueObject.CookCategory;
import com.recipe.myrecipe.recipe.dto.valueObject.CookingInfo;
import com.recipe.myrecipe.recipe.dto.valueObject.IngredientInfo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RecipeDTO {
	private String title;
	private MultipartFile mainImage;
	List<MultipartFile> images;
	List<CookingInfo> cookingInfos;
	List<IngredientInfo> ingredientInfos;
	CookCategory category; 
	Integer time;
	Integer servingSize;
}
