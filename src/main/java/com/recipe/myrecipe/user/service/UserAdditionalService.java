package com.recipe.myrecipe.user.service;

import com.recipe.myrecipe.user.dto.UpdateFeedInfo;
import com.recipe.myrecipe.user.dto.UserFeedInfo;
import com.recipe.myrecipe.user.dto.valueObject.UserNickName;

import java.io.IOException;

public interface UserAdditionalService {
    public UserFeedInfo getUserFeedInfo(String userId);

    public UserFeedInfo getUserFeedInfo(UserNickName userNickName);
    public  UserFeedInfo updateUserFeedInfo(String userId, UpdateFeedInfo updateFeedInfo) throws IOException;
}
