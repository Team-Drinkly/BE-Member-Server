package com.drinkhere.drinklymember.application.oauth.service;

public interface JWTVerifyUseCase {

    void validateToken(final String token);
}
