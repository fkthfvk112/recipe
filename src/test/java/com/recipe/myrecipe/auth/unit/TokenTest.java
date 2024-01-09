package com.recipe.myrecipe.auth.unit;

import com.recipe.myrecipe.auth.dto.RefreshTokenDTO;
import com.recipe.myrecipe.auth.entity.RefreshToken;
import com.recipe.myrecipe.auth.repository.TokenRepository;
import com.recipe.myrecipe.auth.service.TokenService;
import com.recipe.myrecipe.auth.service.impl.TokenServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
public class TokenTest {
    @Mock
    private TokenRepository tokenRepository;
    @InjectMocks
    private TokenServiceImpl tokenService;

    @Test
    void When_givenToken_expect_saveOrReplace() throws Exception{
        RefreshTokenDTO dto = RefreshTokenDTO.builder().refreshToken("testRefresh").userName("testUser").build();
        RefreshToken token = RefreshToken.builder().refreshToken("testRefresh").userName("testUser").build();

        RefreshToken expectToken = RefreshToken.builder().build();
        Optional<RefreshToken> optionalExpectToken = Optional.ofNullable(expectToken);
        Mockito.when(tokenRepository.findByUserName("testUser"))
                .thenReturn(optionalExpectToken);

        boolean result = tokenService.saveOrReplaceRefreshToken(dto);

        verify(tokenRepository, times(1)).findByUserName("testUser");
        verify(tokenRepository, times(1)).save(any(RefreshToken.class));
        Assertions.assertTrue(result, "토큰 저장 반환값이 false입니다.");
    }
}
