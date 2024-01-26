package com.recipe.myrecipe.user.service;

import com.recipe.myrecipe.user.dto.UpdateFeedInfo;
import com.recipe.myrecipe.user.dto.UserFeedInfo;

import java.io.IOException;

public interface UserAdditionalService {
    public UserFeedInfo getUserFeedInfo(String userId);
    public  UserFeedInfo updateUserFeedInfo(String userId, UpdateFeedInfo updateFeedInfo) throws IOException;
}
