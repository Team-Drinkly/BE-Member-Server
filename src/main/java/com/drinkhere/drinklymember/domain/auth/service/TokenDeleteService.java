package com.drinkhere.drinklymember.domain.auth.service;

import java.util.List;

public interface TokenDeleteService {

    void deleteToken(final String key);

    void deleteAllTokens(final List<String> keys);

    void deleteTokenByValue(final String value, final String sub);
}
