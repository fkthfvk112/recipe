package com.recipe.myrecipe.user.service.impl;

import com.recipe.myrecipe.error.BusinessException;
import com.recipe.myrecipe.error.ErrorCode;
import com.recipe.myrecipe.user.dto.UserFeedInfo;
import com.recipe.myrecipe.user.entity.User;
import com.recipe.myrecipe.user.repository.UserRepository;
import com.recipe.myrecipe.user.service.UserAdditionalService;
import org.springframework.stereotype.Service;

@Service
public class UserAdditionalServiceImpl implements UserAdditionalService {

    private UserRepository userRepository;

    UserAdditionalServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserFeedInfo getUserFeedInfo(String userId) {
        User feedOwner = userRepository.findByUserId(userId).orElseThrow(()->{
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        });

        UserFeedInfo userFeedInfo = UserFeedInfo.builder()
                .nickName(feedOwner.getNickName())
                .grantType(feedOwner.getGrantType())
                .email(feedOwner.getEmail())
                .userId(feedOwner.getUserId())
                .userPhoto(feedOwner.getUserPhoto())
                .userUrl(feedOwner.getUserUrl())
                .build();

        return userFeedInfo;
    }
}
