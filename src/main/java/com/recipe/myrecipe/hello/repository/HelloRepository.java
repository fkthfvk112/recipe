package com.recipe.myrecipe.hello.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.myrecipe.hello.entity.Hello;

public interface HelloRepository extends JpaRepository<Hello, Long> {

}
