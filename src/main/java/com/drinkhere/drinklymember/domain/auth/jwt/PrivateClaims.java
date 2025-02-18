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
    private final String role;

    private PrivateClaims(String sub, TokenType tokenType, Boolean isSubscribed, Long subscribeId, String role) {
        this.sub = sub;
        this.tokenType = tokenType;
        this.isSubscribed = isSubscribed != null ? isSubscribed : false;
        this.subscribeId = subscribeId != null ? subscribeId : -1L;
        this.role = role;
    }

    /**
     * JWT Claims를 가변 HashMap으로 생성 (역할에 따라 동적으로 변경)
     */
    public Map<String, Object> createClaimsMap() {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JWTConsts.USER_CLAIMS, sub);
        claims.put(JWTConsts.TOKEN_TYPE, tokenType.name());

        if ("member".equals(role)) {
            claims.put("member-id", sub);
            claims.put("isSubscribed", isSubscribed);
            claims.put("subscribe-id", subscribeId);
        } else if ("owner".equals(role)) {
            claims.put("owner-id", sub);
        }
        return claims;
    }

    /**
     * JWT Payload의 타입 정보 제공 (역할에 따라 키 변경)
     */
    public static Map<String, Class<?>> getClaimsTypeDetailMap() {
        return Map.of(
                JWTConsts.USER_CLAIMS, String.class,
                JWTConsts.TOKEN_TYPE, TokenType.class,
                "member-id", String.class,
                "owner-id", String.class,
                "isSubscribed", Boolean.class,
                "subscribe-id", Long.class
        );
    }

    /**
     * Member 전용 Claims (`member-id` 포함)
     */
    public static PrivateClaims ofMember(String sub, TokenType tokenType, Boolean isSubscribed, Long subscribeId) {
        return new PrivateClaims(sub, tokenType, isSubscribed, subscribeId, "member");
    }

    /**
     * Owner 전용 Claims (`owner-id` 포함)
     */
    public static PrivateClaims ofOwner(String sub, TokenType tokenType) {
        return new PrivateClaims(sub, tokenType, false, -1L, "owner");
    }
}
