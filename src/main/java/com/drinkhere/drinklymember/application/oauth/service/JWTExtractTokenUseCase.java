package com.drinkhere.drinklymember.application.oauth.service;

public interface JWTExtractTokenUseCase {

    String extractToken(final String tokenHeader);
}
