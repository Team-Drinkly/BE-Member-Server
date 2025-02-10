package com.drinkhere.drinklymember.domain.auth.jwt;

import lombok.Getter;

import java.util.Map;

@Getter
public class PrivateClaims {
    private final String sub;
    private final TokenType tokenType;

    public PrivateClaims(String sub, TokenType tokenType) {
        this.sub = sub;
        this.tokenType = tokenType;
    }

    public Map<String, Object> createClaimsMap() {
        return Map.of(
                JWTConsts.USER_CLAIMS, sub,
                JWTConsts.TOKEN_TYPE, tokenType.name()
        );
    }

    public static Map<String, Class<?>> getClaimsTypeDetailMap() {
        return Map.of(
                JWTConsts.USER_CLAIMS, String.class,
                JWTConsts.TOKEN_TYPE, TokenType.class
        );
    }

    public static PrivateClaims of(String sub, TokenType tokenType) {
        return new PrivateClaims(sub, tokenType);
    }
}
