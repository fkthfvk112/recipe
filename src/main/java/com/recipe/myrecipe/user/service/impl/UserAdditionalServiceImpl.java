package com.recipe.myrecipe.user.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.recipe.myrecipe.error.BusinessException;
import com.recipe.myrecipe.error.ErrorCode;
import com.recipe.myrecipe.user.dto.UpdateFeedInfo;
import com.recipe.myrecipe.user.dto.UserFeedInfo;
import com.recipe.myrecipe.user.dto.valueObject.UserNickName;
import com.recipe.myrecipe.user.entity.User;
import com.recipe.myrecipe.user.repository.UserRepository;
import com.recipe.myrecipe.user.service.UserAdditionalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
public class UserAdditionalServiceImpl implements UserAdditionalService {

    private final UserRepository userRepository;
    private final Cloudinary cloudinary;

    UserAdditionalServiceImpl(UserRepository userRepository, Cloudinary cloudinary){
        this.userRepository = userRepository;
        this.cloudinary = cloudinary;
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

    @Override
    public UserFeedInfo getUserFeedInfo(UserNickName userNickName) {
        log.info("[getUserFeedInfo] - start");
        log.info("[getUserFeedInfo] - start " + userNickName.getNickName());

        User feedOwner = userRepository.findByNickName(userNickName.getNickName()).orElseThrow(()->{
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

    @Override
    public UserFeedInfo updateUserFeedInfo(String userId, UpdateFeedInfo updateFeedInfo) throws IOException {
        User feedOwner = userRepository.findByUserId(userId).orElseThrow(()->{
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        });

        if(updateFeedInfo.getUserUrl() != null && updateFeedInfo.getUserUrl() != ""){
            feedOwner.setUserUrl(updateFeedInfo.getUserUrl());
        }
        if(updateFeedInfo.getNickName() != null && updateFeedInfo.getNickName() != ""){
            feedOwner.setNickName(updateFeedInfo.getNickName());
        }

        if(updateFeedInfo.getUserPhoto() != null && updateFeedInfo.getUserPhoto() != ""){
            Map params = ObjectUtils.asMap(
                    "use_filename", true,
                    "unique_filename", false,
                    "overwrite", true
            );
            Object imgObj = (Object)updateFeedInfo.getUserPhoto();
            Map<?, ?> uploadResult = cloudinary.uploader().upload(imgObj, ObjectUtils.emptyMap());
            log.info("[saveImageToAPIserver] - saved repri Img success");
            feedOwner.setUserPhoto(uploadResult.get("secure_url").toString());
        }

        User savedUser = userRepository.save(feedOwner);

        return UserFeedInfo.builder()
                .nickName(savedUser.getNickName())
                .userId(savedUser.getUserId())
                .userPhoto(savedUser.getUserPhoto())
                .email(savedUser.getEmail())
                .userUrl(savedUser.getUserUrl())
                .grantType(savedUser.getGrantType())
                .build();
    }
}
