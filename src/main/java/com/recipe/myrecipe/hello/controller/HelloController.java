package com.recipe.myrecipe.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recipe.myrecipe.hello.repository.HelloRepository;

@RestController
public class HelloController {
	
	@Autowired
	HelloRepository helloRepository;
	
	@GetMapping("/hello")
	ResponseEntity<String> printHello(){
		return ResponseEntity.ok("Hello World");
	}
	
	@GetMapping("/hello2")
	ResponseEntity<String> printHelloDB(){
		return ResponseEntity.ok(helloRepository.findAll().toString());
	}
	
}
