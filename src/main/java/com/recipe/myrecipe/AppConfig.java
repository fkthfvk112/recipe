package com.recipe.myrecipe;

import com.recipe.myrecipe.auth.util.UserUtil;
import com.recipe.myrecipe.util.DtoToEntity;
import org.modelmapper.ModelMapper;
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
}