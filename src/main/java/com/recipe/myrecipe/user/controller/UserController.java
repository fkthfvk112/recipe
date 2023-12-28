package com.recipe.myrecipe.user.controller;

import com.recipe.myrecipe.auth.dto.TokenDTO;
import com.recipe.myrecipe.auth.util.JwtTokenProvider;
import com.recipe.myrecipe.user.dto.SignInResultDTO;
import com.recipe.myrecipe.user.dto.UserLoginDTO;
import com.recipe.myrecipe.user.dto.UserSiginUpDTO;
import com.recipe.myrecipe.user.service.SignService;
import com.recipe.myrecipe.user.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/sign-api")
public class UserController {
    private final JwtTokenProvider jwtTokenProvider;
    private UserService userService;
    private SignService signService;

    @Autowired
    UserController(UserService userService, JwtTokenProvider jwtTokenProvider, SignService signService){
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.signService = signService;
    }

    @PostMapping("/hello")
    public String hello(){
        return "hello world";
    }
    @PostMapping("/sign-in")
    public ResponseEntity<TokenDTO> signIn(@RequestBody UserLoginDTO userLoginDTO){
        log.info("[signIn] - 로그인 시도");
        SignInResultDTO signInResultDTO = signService.signIn(userLoginDTO);
        log.info("[signIn] - 로그인 시도2");

        if(signInResultDTO.isSuccess()){
            log.info("[signIn] - 로그인 성공");
            Authentication authentication = new UsernamePasswordAuthenticationToken(userLoginDTO.getUserId(), null, Collections.emptyList());
            String accessToken = jwtTokenProvider.generateAccessToken(authentication.getName(), null);
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication.getName(), null);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Refresh-Token", refreshToken);

            TokenDTO tokenDTO = TokenDTO.builder().grantType("Bearer")
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            return new ResponseEntity<>(tokenDTO, headers, HttpStatus.OK);
        } else{
            throw new RuntimeException("User not found");
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, String>> signUn(@RequestBody UserSiginUpDTO userSiginUpDTO) {
        if(signService.signUp(userSiginUpDTO)){
            Map<String, String> response = new HashMap<>();
            response.put("status", "200");
            response.put("msg", "signUp successed");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            throw new RuntimeException("sign up fail");
        }
    }
}
