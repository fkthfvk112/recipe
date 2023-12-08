package com.recipe.myrecipe.hello.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@Getter
@Entity
@ToString
@Table(name="hello")
public class Hello {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	Long hello_id;
	
	String name;
}
