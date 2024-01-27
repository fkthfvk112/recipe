package com.recipe.myrecipe.user.controller;

import com.recipe.myrecipe.error.BusinessException;
import com.recipe.myrecipe.error.ErrorCode;
import com.recipe.myrecipe.user.dto.UpdateFeedInfo;
import com.recipe.myrecipe.user.dto.UserFeedInfo;
import com.recipe.myrecipe.user.dto.valueObject.UserNickName;
import com.recipe.myrecipe.user.service.UserAdditionalService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

        return ResponseEntity.ok(userAdditionalService.getUserFeedInfo(authentication.getName()));
    }

    @GetMapping("/user/{nickName}")
    public ResponseEntity<UserFeedInfo> getMyFeedInfo(@PathVariable String nickName){
        log.info("[getMyFeedInfo] - start with " + nickName);
        return ResponseEntity.ok(userAdditionalService.getUserFeedInfo(new UserNickName(nickName)));
    }


    @PostMapping("/update")
    public ResponseEntity<UserFeedInfo> updateUserFeed(@RequestBody @Valid UpdateFeedInfo updateFeedInfo){
        log.info("[updateUserFeed] - start");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);

        try{
            return ResponseEntity.ok(userAdditionalService.updateUserFeedInfo(authentication.getName(), updateFeedInfo));

        }catch(IOException e){
            log.info("[updateUserFeed] - error", e);
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
