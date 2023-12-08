package com.recipe.myrecipe.recipe.entity;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.recipe.myrecipe.recipe.dto.valueObject.CookCategory;
import com.recipe.myrecipe.recipe.dto.valueObject.CookingInfo;
import com.recipe.myrecipe.recipe.dto.valueObject.IngredientInfo;
import jakarta.persistence.*;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Builder
@Table(name="recipe")
public class Recipe {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recipe_id")
	private Long RecipeId;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "main_image_url")
	private String mainImageUrl;
	
	@OneToOne
	@ToString.Exclude
	@JoinColumn(name = "recipe_category_id")
	RecipeCategory recipeCategory;
	
	@ManyToOne
	@ToString.Exclude
	@JoinColumn(name = "recipe_ingredient_info")
}


/*
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
*/