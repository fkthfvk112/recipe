package com.recipe.myrecipe;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.recipe.myrecipe.auth.util.UserUtil;
import com.recipe.myrecipe.util.DtoToEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public DtoToEntity dtoToEntity(){
        return new DtoToEntity();
    }

    @Bean
    public UserUtil userUtil(){ return new UserUtil(); }

    @Value("${cloudinary.cloudName}")
    private String cloudName;

    @Value("${cloudinary.apiKey}")
    private String apiKey;

    @Value("${cloudinary.apiSecret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
        return cloudinary;
    }
}