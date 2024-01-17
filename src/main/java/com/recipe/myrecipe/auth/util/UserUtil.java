package com.recipe.myrecipe.auth.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
public class UserUtil {
    public String getUserId() {
        log.info("[getUserId] - start");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            log.info("[getUserId] - authentication is null");
            throw new IllegalStateException("Authentication is null");
        }
        else{
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String userId = userDetails.getUsername();
            log.info("[getUserId] - userId : {}", userId);

            return userId;
        }

    }
}
