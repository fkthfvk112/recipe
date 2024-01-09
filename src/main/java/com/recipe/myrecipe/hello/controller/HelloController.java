package com.recipe.myrecipe.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.recipe.myrecipe.hello.repository.HelloRepository;

@RequestMapping("/auth-test")
@RestController
public class HelloController {
	
	@Autowired
	HelloRepository helloRepository;
	
	@GetMapping("/hello")
	ResponseEntity<String> printHello(){

		System.out.println("hello");
		return ResponseEntity.ok("Hello World Test");
	}
	
	@PostMapping("/hello2")
	ResponseEntity<String> printHelloDB(){
		return ResponseEntity.ok("Hello World Test");
	}
	
}
