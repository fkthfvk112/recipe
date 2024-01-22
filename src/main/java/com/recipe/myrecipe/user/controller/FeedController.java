package com.recipe.myrecipe.user.controller;

import com.recipe.myrecipe.error.BusinessException;
import com.recipe.myrecipe.error.ErrorCode;
import com.recipe.myrecipe.user.dto.UserFeedInfo;
import com.recipe.myrecipe.user.service.UserAdditionalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("/feed")
public class FeedController {
    private UserAdditionalService userAdditionalService;

    FeedController(UserAdditionalService userAdditionalService){
        this.userAdditionalService = userAdditionalService;
    }

    @GetMapping("/userfeed")
    public ResponseEntity<UserFeedInfo> getUserFeedInfo(@RequestParam String userId){
        log.info("[getUserFeedInfo] - start");
        try{
            return ResponseEntity.ok(userAdditionalService.getUserFeedInfo(userId));
        }catch(BusinessException e){
            throw e;
        }catch (Exception e){
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    @GetMapping("/myfeed")
    public ResponseEntity<UserFeedInfo> getMyFeedInfo(){
        log.info("[getUserFeedInfo] - start");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);

        try{
            return ResponseEntity.ok(userAdditionalService.getUserFeedInfo(authentication.getName()));
        }catch(BusinessException e){
            throw e;
        }catch (Exception e){
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}