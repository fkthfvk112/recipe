package com.recipe.myrecipe.user.service;

import com.recipe.myrecipe.user.dto.SignInResultDTO;
import com.recipe.myrecipe.user.dto.SignUpResultDTO;

public interface SignService {
    SignUpResultDTO signUp(String id, String password, String email, String role);
    SignInResultDTO signIn(String id, String password, String email, String role);
}
