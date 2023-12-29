package com.recipe.myrecipe.auth.filter;

import com.recipe.myrecipe.auth.dto.TokenDTO;
import com.recipe.myrecipe.auth.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("[doFilter] - 필터 시작");
        TokenDTO token = resolveToken((HttpServletRequest) servletRequest);
        log.info("[doFilter] - 토큰값: {}", token);

        //토큰 검사
        log.info("[doFilter] - 토큰 유효성 테스트 시작");
        if(token != null && jwtTokenProvider.isValidateToken(token.getAccessToken())){
            log.info("[doFilter] - if내부 토큰값 : {}", token.getAccessToken());
            log.info("[doFilter] - 토큰 유효성 테스트 결과 : true");
            Authentication authentication = jwtTokenProvider.getAuthentication(token.getAccessToken());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        log.info("[doFilter] - 토큰 유효성 테스트 결과 : false");

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private TokenDTO resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        log.info("[resolveToken] - 토큰값: {}", bearerToken);

        /*
        headers: {
            'Authorization': `Bearer ${tokens.accessToken}`,
            'Refresh-Token': tokens.refreshToken,
          }
         */
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return TokenDTO.builder().accessToken(bearerToken.substring(7))
                    .refreshToken(request.getHeader("Refresh-Token"))
                    .build();
        }
        return null;
    }
}
