package com.recipe.myrecipe.user.service.impl;

import com.recipe.myrecipe.auth.util.JwtTokenProvider;
import com.recipe.myrecipe.user.dto.SignInResultDTO;
import com.recipe.myrecipe.user.dto.UserLoginDTO;
import com.recipe.myrecipe.user.dto.UserSiginUpDTO;
import com.recipe.myrecipe.user.entity.User;
import com.recipe.myrecipe.user.repository.UserRepository;
import com.recipe.myrecipe.user.service.SignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public boolean signUp(UserSiginUpDTO userSiginUpDTO) {
        log.info("[SignServiceImpl-signUp] - 회원가입 시도");
        User user = User.builder()
                .userId(userSiginUpDTO.getUserId())
                .password(passwordEncoder.encode(userSiginUpDTO.getUserPassword()))
                .email(userSiginUpDTO.getEmail())
                .grantType(userSiginUpDTO.getGrantType()).build();

        User savedUser = userRepository.save(user);
        if(!savedUser.getUserId().isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    public SignInResultDTO signIn(UserLoginDTO userLoginDTO) throws RuntimeException{
        log.info("[SignServiceImpl-signIn] - 로그인 시도");
        User user = userRepository.getByUserId(userLoginDTO.getUserId()).get();//if not throw NoSuchElementException

        if(!passwordEncoder.matches(userLoginDTO.getUserPassword(), user.getPassword())){
            throw new RuntimeException();
        }
        log.info("[SignServiceImpl-signIn] - 패스워드 일치");

        SignInResultDTO signInResultDTO = SignInResultDTO.builder()
                .refreshToken(jwtTokenProvider.generateRefreshToken(user.getUserId(), user.getRoles()))
                .accessToken(jwtTokenProvider.generateAccessToken(user.getUserId(), user.getRoles()))
                .isSuccess(true)
                .build();
        return signInResultDTO;
    }
}
