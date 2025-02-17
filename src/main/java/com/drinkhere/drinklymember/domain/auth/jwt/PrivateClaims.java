package com.drinkhere.drinklymember.domain.auth.jwt;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PrivateClaims {
    private final String sub;
    private final TokenType tokenType;
    private final Boolean isSubscribed;
    private final Long subscribeId;

    private PrivateClaims(String sub, TokenType tokenType, Boolean isSubscribed, Long subscribeId) {
        this.sub = sub;
        this.tokenType = tokenType;
        this.isSubscribed = isSubscribed != null ? isSubscribed : false;
        this.subscribeId = subscribeId != null ? subscribeId : -1L;
    }

    /**
     * JWT Claims를 동적으로 만들 수 있도록 가변 HashMap 사용
     */
    public Map<String, Object> createClaimsMap() {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JWTConsts.USER_CLAIMS, sub);
        claims.put(JWTConsts.TOKEN_TYPE, tokenType.name());
        claims.put("user-id", sub);
        claims.put("isSubscribed", isSubscribed);
        claims.put("subscribe-id", subscribeId);
        return claims;
    }

    /**
     * JWT Payload의 타입 정보 제공
     */
    public static Map<String, Class<?>> getClaimsTypeDetailMap() {
        return Map.of(
                JWTConsts.USER_CLAIMS, String.class,
                JWTConsts.TOKEN_TYPE, TokenType.class,
                "user-id", String.class,
                "isSubscribed", Boolean.class,
                "subscribe-id", Long.class
        );
    }

    /**
     * Member 전용 Claims (구독 여부 및 구독 ID 포함)
     */
    public static PrivateClaims ofMember(String sub, TokenType tokenType, Boolean isSubscribed, Long subscribeId) {
        return new PrivateClaims(sub, tokenType, isSubscribed, subscribeId);
    }

    /**
     * Owner 전용 Claims (구독 여부 및 구독 ID 제외, 기본값 적용)
     */
    public static PrivateClaims ofOwner(String sub, TokenType tokenType) {
        return new PrivateClaims(sub, tokenType, false, -1L);
    }
}
