package com.recipe.myrecipe.auth.service;

import com.recipe.myrecipe.auth.dto.RefreshTokenDTO;
import org.springframework.stereotype.Service;

public interface TokenService {
    public boolean saveOrReplaceRefreshToken(RefreshTokenDTO dto);
}
