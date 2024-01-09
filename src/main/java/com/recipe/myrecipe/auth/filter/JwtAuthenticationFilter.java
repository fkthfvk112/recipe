package com.recipe.myrecipe.auth.filter;

import com.recipe.myrecipe.auth.dto.TokenDTO;
import com.recipe.myrecipe.auth.util.JwtTokenProvider;
import com.recipe.myrecipe.error.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.info("[doFilter] - 요청 메소드" + request.getMethod());

        log.info("[doFilter] - 필터 시작" + request.getRequestURL());
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            System.out.println("헤더 정보" + headerName + ": " + headerValue);
        }

        TokenDTO token = resolveToken(request);
        log.info("[doFilter] - 토큰값: {}", token);

        //토큰 검사
        log.info("[doFilter] - 토큰 유효성 테스트 시작");
        if(token != null ){
            if(jwtTokenProvider.isValidateToken(token.getAccessToken())){
                log.info("[doFilter] - 토큰 유효성 테스트 결과 : true");
                Authentication authentication = jwtTokenProvider.getAuthentication(token.getAccessToken());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            if(jwtTokenProvider.isExpiredAccessToken(token.getAccessToken())){
                log.info("[doFilter] -  토큰 유효기간 초과");
                if(request.getMethod().equals("POST")){
                    log.info("[doFilter] - post임");
                    request.getRequestDispatcher("/sign-api/get-accesstoken-post").forward(request, response);
                }
                if(request.getMethod().equals("GET")){
                    log.info("[doFilter] - get임");

                    request.getRequestDispatcher("/sign-api/get-accesstoken").forward(request, response);
                }
                return;
            }
        }
        log.info("[doFilter] - 토큰 유효성 테스트 결과 : false");
        filterChain.doFilter(request, response);
    }

    private TokenDTO resolveToken(HttpServletRequest request) throws UnsupportedEncodingException {
        System.out.println("요청" + request.getRemoteHost());
        //String bearerToken = request.getHeader("Authorization");
        Cookie[] cookies = request.getCookies();//만료 기간 지나면 포함 X 공백 있으면 포함 X
        if (cookies != null && cookies.length > 0) {
            // 모든 쿠키에 대해 반복하여 확인

            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();

                System.out.println("쿠키 이름: " + name);
                System.out.println("쿠키 값: " + value);
            }
        } else {
            System.out.println("클라이언트로부터 쿠키가 전송되지 않았습니다.");
        }

        String bearerToken = null;
        if(cookies != null){
            for (Cookie cookie : cookies) {
                System.out.println("이름:" + cookie.getName());
                if (cookie.getName().startsWith("Authorization") && cookie.getValue().length() >= 14) {
                    bearerToken = cookie.getValue();
                    System.out.println("디코드 된 값 " + bearerToken);
                    break;
                }
            }
        }
        log.info("[resolveToken] - 토큰값: {}", bearerToken);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer_")) {
            return TokenDTO.builder().accessToken(bearerToken.substring("Bearer_".length()))
                    .refreshToken(request.getHeader("Refresh-Token"))
                    .build();
        }
        return null;
    }


}
