package com.drinkhere.drinklymember.application.oauth.service;

public interface JWTExtractUserDetailsUseCase<T> {

    T extract(final String token);
}
