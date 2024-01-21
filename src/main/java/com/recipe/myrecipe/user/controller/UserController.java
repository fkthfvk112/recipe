package com.recipe.myrecipe.user.controller;

import com.recipe.myrecipe.auth.dto.RefreshTokenDTO;
import com.recipe.myrecipe.auth.dto.TokenDTO;
import com.recipe.myrecipe.auth.entity.RefreshToken;
import com.recipe.myrecipe.auth.repository.TokenRepository;
import com.recipe.myrecipe.auth.service.TokenService;
import com.recipe.myrecipe.auth.util.JwtTokenProvider;
import com.recipe.myrecipe.error.BusinessException;
import com.recipe.myrecipe.error.ErrorCode;
import com.recipe.myrecipe.user.dto.SignInResultDTO;
import com.recipe.myrecipe.user.dto.UserAndRefreshDTO;
import com.recipe.myrecipe.user.dto.UserLoginDTO;
import com.recipe.myrecipe.user.dto.UserSiginUpDTO;
import com.recipe.myrecipe.user.service.SignService;
import com.recipe.myrecipe.user.service.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/sign-api")
public class UserController {

    @Value("${cookieDomain}")
    private String cookieDomain;
    private final JwtTokenProvider jwtTokenProvider;
    private UserService userService;
    private SignService signService;
    private TokenService tokenService;
    private TokenRepository tokenRepository;

    @Autowired
    UserController(UserService userService, JwtTokenProvider jwtTokenProvider,
                   SignService signService, TokenService tokenService,
                   TokenRepository tokenRepository){
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.signService = signService;
        this.tokenService = tokenService;
        this.tokenRepository = tokenRepository;
    }

    @GetMapping("/hello")
    public String hello(){
        log.info("[hello] - hello");

        return "hello world";
    }

    @PostMapping("/hello2")
    public String hello2(){
        log.info("[hello] - hello");

        return "hello world";
    }
    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@RequestBody UserLoginDTO userLoginDTO){

//    public ResponseEntity<String> signIn(@RequestBody UserLoginDTO userLoginDTO){
        log.info("[signIn] - 로그인 시도");
        SignInResultDTO signInResultDTO = signService.signIn(userLoginDTO);

        if(signInResultDTO.isSuccess()){
            log.info("[signIn] - 로그인 성공");
            Authentication authentication = new UsernamePasswordAuthenticationToken(userLoginDTO.getUserId(), null, Collections.emptyList());
            String accessToken = jwtTokenProvider.generateAccessToken(authentication.getName(), List.of("USER"));
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication.getName(), List.of("USER"));

            tokenService.saveOrReplaceRefreshToken(RefreshTokenDTO.builder()
                    .userName(userLoginDTO.getUserId())
                    .refreshToken(refreshToken)
                    .build());

            HttpHeaders headers = new HttpHeaders();

            String cookiePath = "/"; // 쿠키의 유효 경로를 애플리케이션 루트로 설정
//            String cookieDomain = "localhost"; // 쿠키의 유효 도메인을 설정 (도메인이 localhost인 경우)
            String accessTokenCookie = String.format("%s=%s; Path=%s; Domain=%s; SameSite=None; Secure; HttpOnly", "Authorization", "Bearer_" + accessToken, cookiePath, cookieDomain);
            String refreshTokenCookie = String.format("%s=%s; Path=%s; Domain=%s; SameSite=None; Secure", "Refresh-token", refreshToken, cookiePath, cookieDomain);

            headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie);
            headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie);

            return new ResponseEntity<>("signIn Success", headers, HttpStatus.OK);
        } else{
            throw new RuntimeException("User not found");
        }
    }

    @GetMapping("/token")
    public ResponseEntity<String> tokenTest(){
        System.out.println("토큰");
        String cookiePath = "/"; // 쿠키의 유효 경로를 애플리케이션 루트로 설정
        String cookieDomain = "localhost"; // 쿠키의 유효 도메인을 설정 (도메인이 localhost인 경우)

        HttpHeaders headers = new HttpHeaders();
        String cookie = String.format("%s=%s; Path=%s; Domain=%s; SameSite=None; Secure", "hello", "world", cookiePath, cookieDomain);

        headers.add(HttpHeaders.SET_COOKIE, cookie);

            return new ResponseEntity<>("hello", headers, HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, String>> signUn(@RequestBody UserSiginUpDTO userSiginUpDTO) {
        log.info("signUn start");
        if(signService.signUp(userSiginUpDTO)){
            Map<String, String> response = new HashMap<>();
            response.put("status", "200");
            response.put("msg", "signUp successed");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            throw new RuntimeException("sign up fail");
        }
    }

    @GetMapping("/get-accesstoken")
    public ResponseEntity<String> getAccessTokenFromRefreshToken(HttpServletRequest request){
        log.info("[getAccessTokenFromRefreshToken] - 시작");

        String refreshToken = jwtTokenProvider.getRefreshTokenValue(request);
        log.info("[getAccessTokenFromRefreshToken] - refreshToken :{}", refreshToken);
        if(refreshToken == null) throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);

        String userName = jwtTokenProvider.getUserName(refreshToken);

        String savedRefreshTokenString = tokenRepository.findByUserName(userName).get().getRefreshToken();
        System.out.println("유저 네임" + jwtTokenProvider.getUserName(savedRefreshTokenString));

        if(!savedRefreshTokenString.equals(refreshToken)){
            log.info("[getAccessTokenFromRefreshToken] - DB 정보와 토큰 일치 X");
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_COLLECT_DB);
        }

        if(jwtTokenProvider.isValidateToken(refreshToken)){
            log.info("[getAccessTokenFromRefreshToken] - 리프래쉬 검증 성공");

            String accessToken = jwtTokenProvider.generateAccessToken(userName, List.of("USER"));

            HttpHeaders headers = new HttpHeaders();
            String cookiePath = "/"; // 쿠키의 유효 경로를 애플리케이션 루트로 설정
            String accessTokenCookie = String.format("%s=%s; Path=%s; Domain=%s; SameSite=None; Secure; HttpOnly",
                    "Authorization", "Bearer_" + accessToken, cookiePath, cookieDomain);
            headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie);
            //headers.add(HttpHeaders.COOKIE, accessToken);

            return new ResponseEntity<>("Issue new token success", headers, HttpStatus.OK);
        }else{
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_VALID);
        }
    }

    @PostMapping("/get-accesstoken-post")
    public ResponseEntity<String> getAccessTokenFromRefreshTokenPost(HttpServletRequest request){
        log.info("[getAccessTokenFromRefreshToken] - 시작");

        String refreshToken = jwtTokenProvider.getRefreshTokenValue(request);
        log.info("[getAccessTokenFromRefreshToken] - refreshToken :{}", refreshToken);
        if(refreshToken == null) throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);

        String userName = jwtTokenProvider.getUserName(refreshToken);

        String savedRefreshTokenString = tokenRepository.findByUserName(userName).get().getRefreshToken();
        System.out.println("유저 네임" + jwtTokenProvider.getUserName(savedRefreshTokenString));

        if(!savedRefreshTokenString.equals(refreshToken)){
            log.info("[getAccessTokenFromRefreshToken] - DB 정보와 토큰 일치 X");
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_COLLECT_DB);
        }

        if(jwtTokenProvider.isValidateToken(refreshToken)){
            log.info("[getAccessTokenFromRefreshToken] - 리프래쉬 검증 성공");

            String accessToken = jwtTokenProvider.generateAccessToken(userName, List.of("USER"));

            HttpHeaders headers = new HttpHeaders();
            String cookiePath = "/"; // 쿠키의 유효 경로를 애플리케이션 루트로 설정
            String accessTokenCookie = String.format("%s=%s; Path=%s; Domain=%s; SameSite=None; Secure; HttpOnly",
                    "Authorization", "Bearer_" + accessToken, cookiePath, cookieDomain);
            headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie);
            //headers.add(HttpHeaders.COOKIE, accessToken);

            return new ResponseEntity<>("Issue new token success", headers, HttpStatus.OK);
        }else{
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_VALID);
        }
    }

    @GetMapping("/access-expired")
    public void accessExpired(){
        log.info("[accessExpired]");
        throw new BusinessException(ErrorCode.ACCESS_TOKEN_EXPIRED);
    }
}
