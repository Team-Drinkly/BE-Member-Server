package com.drinkhere.drinklymember.domain.auth.handler.request;

import com.drinkhere.drinklymember.domain.auth.enums.Authority;
import com.drinkhere.drinklymember.domain.auth.enums.Provider;

public record OAuthSuccessEvent(
        String username,
        String email,
        Provider provider,
        String sub,
        Long userId,
        Authority authority
) {
    public static OAuthSuccessEvent of(String username, String email, Provider provider, String sub, Long userId, Authority authority) {
        return new OAuthSuccessEvent(username, email, provider, sub, userId, authority);
    }
}
