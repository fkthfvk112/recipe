package com.recipe.myrecipe.user.service.impl;

import com.recipe.myrecipe.auth.util.JwtTokenProvider;
import com.recipe.myrecipe.user.dto.SignInResultDTO;
import com.recipe.myrecipe.user.dto.SignUpResultDTO;
import com.recipe.myrecipe.user.entity.User;
import com.recipe.myrecipe.user.repository.UserRepository;
import com.recipe.myrecipe.user.service.SignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SignServiceImpl implements SignService {
    public UserRepository userRepository;
    public JwtTokenProvider jwtTokenProvider;
    public PasswordEncoder passwordEncoder;

    @Autowired
    SignServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public SignUpResultDTO signUp(String id, String password, String email, String role) {
        User user;
        
        return null;
    }

    @Override
    public SignInResultDTO signIn(String id, String password, String email, String role) throws RuntimeException{
        log.info("[SignInResultDTO] - 로그인 시도");
        User user = userRepository.getByUserId(id).get();//if not throw NoSuchElementException

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException();
        }
        log.info("[SignInResultDTO] - 패스워드 일치");

        SignInResultDTO signInResultDTO = SignInResultDTO.builder()
                .refreshToken(jwtTokenProvider.generateRefreshToken(user.getUserId(), user.getRoles()))
                .accessToken(jwtTokenProvider.generateAccessToken(user.getUserId(), user.getRoles()))
                .build();
        return signInResultDTO;
    }
}
