package com.recipe.myrecipe.recipe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeCategory {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="recipe_category_id")
	private Long RecipeCategoryId;
	
	@Column(name="sort")
	String sort;//음료, 디저트, 면 등
	
	@Column(name="condition")
	String condition;//아플 떄 등
	
	@Column(name="technique")
	String technique;//굽기, 튀기기 등
}
