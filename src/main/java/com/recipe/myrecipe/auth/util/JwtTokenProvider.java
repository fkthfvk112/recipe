package com.recipe.myrecipe.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long ACCESS_EXPIRATION_TIME = 3 * 60 * 60 * 1000;//3시간
    private static final long REFRESH_EXPIRATION_TIME = 30 * 60 * 1000;//30분

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
                .signWith(SECRET_KEY)
                .compact();
        log.info("[generateToken] 토큰 생성 완료");

        return jwts;
    }

    public boolean isValidateToken(String token) {
        log.info("[isValidateToken] - accesstoken : ${}", token);
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("[isValidateToken] : Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("[isValidateToken] : Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("[isValidateToken] : Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("[isValidateToken] : JWT claims string is empty.", e);
        }
        return false;
    }

    private String resolveToken(HttpServletRequest request){
        log.info("[resolveToken] 토큰 헤더에서 추출");
        return request.getHeader("X-AUTH-TOKEN");
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }


        Collection<? extends GrantedAuthority> authorities =
                ((List<?>) claims.get("auth")).stream()
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
}
