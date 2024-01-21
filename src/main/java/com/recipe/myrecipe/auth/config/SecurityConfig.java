package com.recipe.myrecipe.auth.config;
import com.recipe.myrecipe.auth.filter.JwtAuthenticationFilter;
import com.recipe.myrecipe.auth.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨.
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http

                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .cors((cors) -> cors
                        .configurationSource(corsConfigurationSource())
                )
                .csrf(AbstractHttpConfigurer::disable)//추후 켜고 보안 추가
                // 특정 URL에 대한 권한 설정.
                .authorizeHttpRequests((authorizeRequests) -> {
                    authorizeRequests.requestMatchers("/sign-api/**").permitAll(); // 권한 없어도 열람 가능
                    authorizeRequests.requestMatchers("/recipe/create").authenticated(); //제한
                    authorizeRequests.requestMatchers("/recipe/get-my-recipe").authenticated();
                    authorizeRequests.requestMatchers("/auth-test/**").authenticated(); //제한
                    authorizeRequests.requestMatchers("/manager/**")
                            // ROLE_은 붙이면 안 된다. hasAnyRole()을 사용할 때 자동으로 ROLE_이 붙기 때문이다.
                            .hasAnyRole("ADMIN", "MANAGER");

                    authorizeRequests.requestMatchers("/admin/**")
                            // ROLE_은 붙이면 안 된다. hasRole()을 사용할 때 자동으로 ROLE_이 붙기 때문이다.
                            .hasRole("ADMIN");

                    authorizeRequests.anyRequest().permitAll();
                })
                .build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://localhost:3001"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}