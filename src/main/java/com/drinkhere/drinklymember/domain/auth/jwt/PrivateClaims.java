package com.drinkhere.drinklymember.domain.auth.jwt;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PrivateClaims {
    private final String sub;
    private final TokenType tokenType;
    private final Boolean isSubscribed; // 회원 구독 여부 추가

    private PrivateClaims(String sub, TokenType tokenType, Boolean isSubscribed) {
        this.sub = sub;
        this.tokenType = tokenType;
        this.isSubscribed = isSubscribed;
    }

    /**
     * JWT Claims를 동적으로 만들 수 있도록 가변 HashMap 사용
     */
    public Map<String, Object> createClaimsMap() {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JWTConsts.USER_CLAIMS, sub);
        claims.put(JWTConsts.TOKEN_TYPE, tokenType.name());
        claims.put("user-id", sub);  // "user-id" 필드 추가
        if (isSubscribed != null) {
            claims.put("isSubscribed", isSubscribed); // 구독 여부 추가
        }
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
                "isSubscribed", Boolean.class // 구독 여부 타입 추가
        );
    }

    /**
     * Member 전용 Claims (구독 여부 포함)
     */
    public static PrivateClaims ofMember(String sub, TokenType tokenType, boolean isSubscribed) {
        return new PrivateClaims(sub, tokenType, isSubscribed);
    }

    /**
     * Owner 전용 Claims (구독 여부 제외)
     */
    public static PrivateClaims ofOwner(String sub, TokenType tokenType) {
        return new PrivateClaims(sub, tokenType, null);
    }
}
