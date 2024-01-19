package com.recipe.myrecipe.recipe.dto.valueObject;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Setter
@Getter
public class RepriPhoto {
    MultipartFile infoImg;
}
