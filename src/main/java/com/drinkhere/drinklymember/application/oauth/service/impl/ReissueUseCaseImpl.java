package com.drinkhere.drinklymember.application.oauth.service.impl;

import com.drinkhere.drinklymember.application.oauth.service.ReissueUseCase;
import com.drinkhere.drinklymember.common.annotation.ApplicationService;
import com.drinkhere.drinklymember.domain.auth.consts.AuthConsts;
import com.drinkhere.drinklymember.domain.auth.dto.Token;
import com.drinkhere.drinklymember.domain.auth.dto.TokenReissueResponse;
import com.drinkhere.drinklymember.domain.auth.jwt.JWTProvider;
import com.drinkhere.drinklymember.domain.auth.jwt.TokenType;
import com.drinkhere.drinklymember.domain.auth.service.TokenDeleteService;
import com.drinkhere.drinklymember.domain.auth.service.TokenLockService;
import com.drinkhere.drinklymember.domain.auth.service.TokenSaveService;
import com.drinkhere.drinklymember.domain.auth.service.TokenValidateService;
import com.drinkhere.drinklymember.domain.member.service.member.MemberQueryService;
import com.drinkhere.drinklymember.domain.member.service.owner.OwnerQueryService;
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
    private final MemberQueryService memberQueryService;  // 회원인지 확인하는 서비스 추가
    private final OwnerQueryService ownerQueryService;    // 사장님인지 확인하는 서비스 추가

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
                return generateToken(jwtProvider::getCachedToken, refreshToken, sub);
            }
            tokenValidateService.validateIsExistToken(refreshToken, TokenType.REFRESH_TOKEN, sub);
            tokenDeleteService.deleteTokenByValue(refreshToken, sub);

            return generateAndSaveToken(sub);
        } finally {
            tokenLockService.releaseLockToken(lockKey); // Redis 락 해제
        }
    }

    /**
     * 토큰 재발급 시 회원(Member) 또는 사장님(Owner) 구분하여 생성
     */
    private TokenReissueResponse generateAndSaveToken(final String sub) {

        final Token token;

        if (isMember(sub)) {
            token = jwtProvider.generateMemberToken(Long.parseLong(sub));
        } else if (isOwner(sub)) {
            token = jwtProvider.generateOwnerToken(Long.parseLong(sub));
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }

        tokenSaveService.saveToken(token.refreshToken(), TokenType.REFRESH_TOKEN, sub);
        return generateToken(inputRefreshToken -> token, token.refreshToken(), sub);
    }

    private TokenReissueResponse generateToken(final Function<String, Token> tokenGenerator, final String refreshToken, final String sub) {
        final Token token = tokenGenerator.apply(refreshToken);
        final String generatedAccessToken = AuthConsts.AUTHENTICATION_TYPE_PREFIX + token.accessToken();
        final String generatedRefreshToken = AuthConsts.AUTHENTICATION_TYPE_PREFIX + token.refreshToken();
        return new TokenReissueResponse(generatedAccessToken, generatedRefreshToken);
    }

    /**
     * 회원(Member)인지 확인
     */
    private boolean isMember(String sub) {
        return memberQueryService.existsById(Long.parseLong(sub));
    }

    /**
     * 사장님(Owner)인지 확인
     */
    private boolean isOwner(String sub) {
        return ownerQueryService.existsById(Long.parseLong(sub));
    }
}
