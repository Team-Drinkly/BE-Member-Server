package com.drinkhere.drinklymember.application.oauth.service.impl;

import com.drinkhere.drinklymember.application.oauth.service.ReissueUseCase;
import com.drinkhere.drinklymember.common.annotation.ApplicationService;
import com.drinkhere.drinklymember.domain.auth.consts.AuthConsts;
import com.drinkhere.drinklymember.domain.auth.dto.TokenReissueResponse;
import com.drinkhere.drinklymember.domain.auth.jwt.JWTProvider;
import com.drinkhere.drinklymember.domain.auth.jwt.TokenType;
import com.drinkhere.drinklymember.domain.auth.service.TokenDeleteService;
import com.drinkhere.drinklymember.domain.auth.service.TokenLockService;
import com.drinkhere.drinklymember.domain.auth.service.TokenSaveService;
import com.drinkhere.drinklymember.domain.auth.service.TokenValidateService;
import com.drinkhere.drinklymember.util.TokenExtractUtils;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
@ApplicationService
public class ReissueUseCaseImpl implements ReissueUseCase {

    private final JWTProvider jwtProvider;
    private final TokenDeleteService tokenDeleteService;
    private final TokenSaveService tokenSaveService;
    private final TokenValidateService tokenValidateService;
    private final TokenLockService tokenLockService;

    @Override
    public TokenReissueResponse reissue(String refreshTokenHeader) {
        final String refreshToken = TokenExtractUtils.extractToken(refreshTokenHeader);
        jwtProvider.validateToken(refreshToken, TokenType.REFRESH_TOKEN);
        final String sub = jwtProvider.extractSubFromToken(refreshToken, TokenType.REFRESH_TOKEN);
        return reissueToken(refreshToken, sub);
    }

    private TokenReissueResponse reissueToken(final String refreshToken, final String sub) {
        final String lockKey = sub; // sub를 락 키로 사용
        try {
            tokenLockService.lockToken(lockKey); // Redis를 사용한 락
            if (jwtProvider.existsCachedRefreshToken(refreshToken)) {
                return generateToken(jwtProvider::getCachedToken, refreshToken);
            }
            tokenValidateService.validateIsExistToken(refreshToken, TokenType.REFRESH_TOKEN, sub);
            tokenDeleteService.deleteTokenByValue(refreshToken, sub);

            return generateAndSaveToken(jwtProvider::reIssueToken, refreshToken, sub);
        } finally {
            tokenLockService.releaseLockToken(lockKey); // Redis 락 해제
        }
    }

    private TokenReissueResponse generateToken(final Function<String, JWTProvider.Token> tokenGenerator, final String refreshToken) {
        final JWTProvider.Token token = tokenGenerator.apply(refreshToken);
        final String generatedAccessToken = AuthConsts.AUTHENTICATION_TYPE_PREFIX + token.accessToken();
        final String generatedRefreshToken = AuthConsts.AUTHENTICATION_TYPE_PREFIX + token.refreshToken();
        return new TokenReissueResponse(generatedAccessToken, generatedRefreshToken);
    }

    private TokenReissueResponse generateAndSaveToken(final Function<String, JWTProvider.Token> tokenGenerator, final String refreshToken, final String sub) {
        final JWTProvider.Token token = tokenGenerator.apply(refreshToken);
        tokenSaveService.saveToken(token.refreshToken(), TokenType.REFRESH_TOKEN, sub);
        return generateToken(inputRefreshToken -> token, refreshToken);
    }
}
