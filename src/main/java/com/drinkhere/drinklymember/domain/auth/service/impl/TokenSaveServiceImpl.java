package com.drinkhere.drinklymember.domain.auth.service.impl;

import com.drinkhere.drinklymember.common.annotation.DomainService;
import com.drinkhere.drinklymember.domain.auth.jwt.TokenType;
import com.drinkhere.drinklymember.domain.auth.service.TokenSaveService;
import com.drinkhere.drinklymember.redis.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

@DomainService
@RequiredArgsConstructor
public class TokenSaveServiceImpl implements TokenSaveService {

    private final RedisUtil redisUtil;

    @Value("${jwt.access-token-expiration-time}")
    private Long tokenExpirationTime; // 토큰 만료 시간 (초 단위)

    @Override
    public void saveToken(String token, TokenType tokenType, String sub) {
        // Redis에 저장할 key 생성 (예: "TOKEN:sub:tokenType")
        String key = String.format("TOKEN:%s:%s", sub, tokenType.name());

        // Redis에 토큰 저장
        redisUtil.saveAsValue(key, token, tokenExpirationTime, TimeUnit.SECONDS);
    }
}
