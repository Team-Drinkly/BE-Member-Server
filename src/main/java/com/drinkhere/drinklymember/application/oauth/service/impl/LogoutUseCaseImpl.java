package com.drinkhere.drinklymember.application.oauth.service.impl;

import com.drinkhere.drinklymember.application.oauth.service.LogoutUseCase;
import com.drinkhere.drinklymember.common.annotation.ApplicationService;
import com.drinkhere.drinklymember.domain.auth.jwt.JWTProvider;
import com.drinkhere.drinklymember.domain.auth.jwt.TokenType;
import com.drinkhere.drinklymember.domain.auth.service.TokenDeleteService;
import com.drinkhere.drinklymember.redis.util.RedisUtil;
import com.drinkhere.drinklymember.util.TokenExtractUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationService
@RequiredArgsConstructor
public class LogoutUseCaseImpl implements LogoutUseCase {

    private final TokenDeleteService tokenDeleteService;
    private final RedisUtil redisUtil;
    private final JWTProvider jwtProvider;

    @Override
    public void logoutAccessUser(String refreshTokenHeader) {
        final String refreshToken = TokenExtractUtils.extractToken(refreshTokenHeader);

        try {
            // 사용자 ID 추출
            final Long userId = 0L;

            // Redis Key 생성
            final String redisKey = generateRedisKey(userId);

            // Redis에서 토큰 조회
            Object storedValue = redisUtil.get(redisKey);
            if (refreshToken.equals(storedValue)) {
                // Redis에서 토큰 삭제
                tokenDeleteService.deleteToken(redisKey);
                log.info("Logout successful. Token removed from Redis for user ID {}: {}", userId, redisKey);
            } else {
                log.warn("Token mismatch or not found in Redis for user ID {}: {}", userId, redisKey);
            }
        } catch (Exception e) {
            log.error("Logout failed due to an unexpected error: {}", e.getMessage(), e);
            throw new IllegalStateException("Logout process failed. Please try again later.", e);
        }
    }

    /**
     * Redis Key 생성 유틸 메서드.
     *
     * @param userId 사용자 ID
     * @return Redis Key
     */
    private String generateRedisKey(Long userId) {
        return String.format("TOKEN:%d:%s", userId, TokenType.REFRESH_TOKEN.name());
    }
}
