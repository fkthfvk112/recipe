package com.recipe.myrecipe.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;
   // private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Value("${springboot.jwt.secret}")
    private String secretKey = "secretKey";
    private static final long ACCESS_EXPIRATION_TIME = 5000;//5초 ... 수정
    private static final long REFRESH_EXPIRATION_TIME = 3 * 60 * 60 * 1000;//3시간

    @PostConstruct
    protected void init(){
        log.info("[JwtTokenProvider-init] 초기화 시작 - secretKey : ${}", secretKey);
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        log.info("[JwtTokenProvider-init] 초기화 완료 - secretKey : ${}", secretKey );
    }

    public String generateAccessToken(String userId, List<String> roles){
        return generateToken(userId, roles, ACCESS_EXPIRATION_TIME);
    };

    public String generateRefreshToken(String userId, List<String> roles){
        return generateToken(userId, roles, REFRESH_EXPIRATION_TIME);
    };

    public String generateToken(String userId, List<String> roles, long expirationTime){
        log.info("[generateToken] 토큰 생성");

       // Claims claims = Jwts.claims().subject(userId).build();

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);
        log.info("[generateToken] 토큰 생성2");

        String jwts = Jwts.builder()
                .claims(claims)
                .subject(userId)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
        log.info("[generateToken] 토큰 생성 완료");

        return jwts;
    }

    public boolean isValidateToken(String token) {
        log.info("[isValidateToken] - accesstoken : {}", token);
        try {
            Jws<Claims> jws = Jwts.parser().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
            Claims claims = jws.getBody();
            //리프래쉬 토큰와 액세스 토큰 구분
            if(claims.containsKey("roles")){
                log.info("[isValidateToken] 역할 ... ", claims.get("roles"));
                List<String> roles = (List<String>) claims.get("roles");
                if(roles.contains("USER")) {
                    return true;
                }
            }
            log.info("[isValidateToken] 엄슴", claims.get("roles"));
            return false;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("[isValidateToken] : Invalid JWT Token", e);
            return false;
        } catch (ExpiredJwtException e) {
            log.info("[isValidateToken] : Expired JWT Token");
            return false;
        } catch (UnsupportedJwtException e) {
            log.info("[isValidateToken] : Unsupported JWT Token", e);
            return false;
        } catch (IllegalArgumentException e) {
            log.info("[isValidateToken] : JWT claims string is empty.", e);
            return false;
        }
    }

    public boolean isExpiredAccessToken(String token){
        try {
            Jws<Claims> jws = Jwts.parser().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
            Claims claims = jws.getBody();
            return false;
        } catch (ExpiredJwtException e) {
            //log.info("[isExpiredAccessToken] : Expired JWT Token", e);
            return true;
        }
    }

    private String resolveToken(HttpServletRequest request){
        log.info("[resolveToken] 토큰 헤더에서 추출");
        return request.getHeader("X-AUTH-TOKEN");
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token).getBody();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("roles") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }


        Collection<? extends GrantedAuthority> authorities =
                ((List<?>) claims.get("roles")).stream()
                        .map(authority -> new SimpleGrantedAuthority((String) authority))
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public String getUserName(String token){
        log.info("[getUserName] 토큰에서 회원 이름 추출");
        String info = parseClaims(token).getSubject();
        log.info("[getUserName] 토큰에서 회원 이름 추출 완료, info : {}", info);

        return info;
    }

    public String getRefreshTokenValue(HttpServletRequest request) {
        log.info("[getRefreshTokenValue] 리프래쉬 토큰 얻기 시작");

        String refreshTokenValue = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("이름:" + cookie.getName());
                System.out.println("값:" + cookie.getValue());

                if (cookie.getName().startsWith("Refresh-token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
