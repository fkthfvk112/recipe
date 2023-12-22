package com.recipe.myrecipe.user.repository;

import com.recipe.myrecipe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, String> {

}
