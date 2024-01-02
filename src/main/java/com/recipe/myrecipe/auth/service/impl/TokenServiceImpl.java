package com.recipe.myrecipe.auth.service.impl;

import com.recipe.myrecipe.auth.dto.RefreshTokenDTO;
import com.recipe.myrecipe.auth.entity.RefreshToken;
import com.recipe.myrecipe.auth.repository.TokenRepository;
import com.recipe.myrecipe.auth.service.TokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.util.Optional;
@Service
public class TokenServiceImpl implements TokenService {
    private TokenRepository tokenRepository;
    private ModelMapper modelMapper;

    @Autowired
    TokenServiceImpl(TokenRepository tokenRepository, ModelMapper modelMapper){
        this.tokenRepository = tokenRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public boolean saveOrReplaceRefreshToken(RefreshTokenDTO dto) {
        Optional<RefreshToken> refreshTokenOptional  = tokenRepository.findByUserName(dto.getUserName());
        if(refreshTokenOptional.isPresent()){
            RefreshToken modifiedToken = refreshTokenOptional.get();
            modifiedToken.setRefreshToken(dto.getRefreshToken());
            tokenRepository.save(modifiedToken);

            return true;
        }
        RefreshToken newRefreshToken = RefreshToken.builder()
                .refreshToken(dto.getRefreshToken())
                .userName(dto.getUserName()).build();

        tokenRepository.save(newRefreshToken);

        return true;
    }
}
