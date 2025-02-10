package com.drinkhere.drinklymember.application.oauth.service.impl;

import com.drinkhere.drinklymember.application.oauth.service.JWTExtractTokenUseCase;
import com.drinkhere.drinklymember.common.annotation.ApplicationService;
import com.drinkhere.drinklymember.util.TokenExtractUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationService
public class JWTExtractTokenUseCaseImpl implements JWTExtractTokenUseCase {
    @Override
    public String extractToken(final String tokenHeader) {
        return TokenExtractUtils.extractToken(tokenHeader);
    }
}
