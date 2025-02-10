package com.drinkhere.drinklymember.domain.auth.service;

import com.drinkhere.drinklymember.redis.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenLockService {

    private final RedisUtil redisUtil;

    private static final long LOCK_EXPIRATION_TIME = 10; // 락 만료 시간 (초)

    public void lockToken(final String token) {
        boolean isLocked = redisUtil.saveAsValueIfAbsent(token, "LOCKED", LOCK_EXPIRATION_TIME, TimeUnit.SECONDS);
        if (!isLocked) {
            throw new IllegalStateException("Token is already locked: " + token);
        }
    }

    public void releaseLockToken(final String token) {
        redisUtil.delete(token);
    }
}
