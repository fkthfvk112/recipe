package com.recipe.myrecipe.user.controller;

import com.recipe.myrecipe.auth.dto.TokenDTO;
import com.recipe.myrecipe.auth.util.JwtUtil;
import com.recipe.myrecipe.user.dto.UserLoginDTO;
import com.recipe.myrecipe.user.service.UserService;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final JwtUtil jwtUtil;
    private UserService userService;

    @Autowired
    UserController(UserService userService, JwtUtil jwtUtil){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/hello")
    public String hello(){
        return "hello world";
    }
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody UserLoginDTO userLoginDTO){
        if(userService.isUserExist(userLoginDTO)){//존재하면? 으로 수정...service호출
            Authentication authentication = new UsernamePasswordAuthenticationToken(userLoginDTO.getUserId(), null, Collections.emptyList());
            String accessToken = jwtUtil.generateAccessToken(authentication);
            String refreshToken = jwtUtil.generateRefreshToken(authentication);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Refresh-Token", refreshToken);

            TokenDTO tokenDTO = TokenDTO.builder().grantType("Bearer")
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            return new ResponseEntity<>(tokenDTO, headers, HttpStatus.OK);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
