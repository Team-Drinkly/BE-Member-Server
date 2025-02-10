package com.drinkhere.drinklymember.application.oauth.service.impl;

import com.drinkhere.drinklymember.application.oauth.service.JWTVerifyUseCase;
import com.drinkhere.drinklymember.common.annotation.ApplicationService;
import com.drinkhere.drinklymember.domain.auth.jwt.JWTProvider;
import com.drinkhere.drinklymember.domain.auth.jwt.TokenType;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class JWTVerifyUseCaseImpl implements JWTVerifyUseCase {

    private final JWTProvider jwtProvider;

    @Override
    public void validateToken(final String token) {
        jwtProvider.validateToken(token, TokenType.ACCESS_TOKEN);
    }
}
