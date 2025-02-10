package com.drinkhere.drinklymember.domain.auth.service;

import com.drinkhere.drinklymember.domain.auth.jwt.TokenType;

public interface TokenQueryService {

    String findTokenByValue(final String value, final TokenType tokenType);
}