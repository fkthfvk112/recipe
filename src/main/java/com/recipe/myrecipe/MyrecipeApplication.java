package com.recipe.myrecipe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


//수정 : 추후 () 다 없애기
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableJpaAuditing
public class MyrecipeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyrecipeApplication.class, args);
	}

}
