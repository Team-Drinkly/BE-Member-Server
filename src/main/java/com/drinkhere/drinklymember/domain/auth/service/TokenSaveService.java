package com.drinkhere.drinklymember.domain.auth.service;

import com.drinkhere.drinklymember.domain.auth.jwt.TokenType;

public interface TokenSaveService {

    void saveToken(final String token, final TokenType tokenType, final String sub);
}
