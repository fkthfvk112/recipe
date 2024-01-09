package com.recipe.myrecipe.user.service;

import com.recipe.myrecipe.user.dto.SignInResultDTO;
import com.recipe.myrecipe.user.dto.UserLoginDTO;
import com.recipe.myrecipe.user.dto.UserSiginUpDTO;

public interface SignService {
    boolean signUp(UserSiginUpDTO userSiginUpDTO);
    //SignInResultDTO signIn(String id, String password, String email, String role);
    SignInResultDTO signIn(UserLoginDTO userLoginDTO);
}
