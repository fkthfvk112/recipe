package com.recipe.myrecipe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


//수정 : 추후 () 다 없애기
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class MyrecipeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyrecipeApplication.class, args);
	}

}
