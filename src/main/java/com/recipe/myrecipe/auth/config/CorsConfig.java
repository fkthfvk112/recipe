//package com.recipe.myrecipe.auth.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000", "https://localhost:3001")//allowCredentials(true)이면 * 사용 불가
//                .allowedMethods("POST", "GET", "DELETE", "PUT", "OPTIONS")
//                .allowCredentials(true);
//                //.allowedOrigins("http://client");
//    }
//}