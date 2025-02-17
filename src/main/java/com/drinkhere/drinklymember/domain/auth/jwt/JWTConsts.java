package com.drinkhere.drinklymember.domain.auth.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JWTConsts {
    public static final String TOKEN_ISSUER = "drinkly";
    public static final String USER_CLAIMS = "user-id";
    public static final String TOKEN_TYPE = "token_type";
}
