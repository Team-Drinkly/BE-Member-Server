package com.drinkhere.drinklymember.domain.auth.handler.request;

import com.drinkhere.drinklymember.domain.auth.enums.Provider;

public record OAuthSuccessEvent(
    String username,
    String email,
    Provider provider,
    String sub
) {
    public static OAuthSuccessEvent of(String username, String email, Provider provider, String sub) {
        return new OAuthSuccessEvent(username, email, provider, sub);
    }
}
