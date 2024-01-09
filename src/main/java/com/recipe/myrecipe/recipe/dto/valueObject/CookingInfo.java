package com.recipe.myrecipe.recipe.dto.valueObject;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CookingInfo {
	String infoText;
	MultipartFile infoImg;
}
