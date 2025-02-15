package com.drinkhere.drinklymember.domain.auth.handler.request;

import com.drinkhere.drinklymember.domain.auth.enums.Provider;

public record OAuthSuccessEvent(
        String username,
        String email,
        Provider provider,
        String sub,
        Long userId  // 추가
) {
    public static OAuthSuccessEvent of(String username, String email, Provider provider, String sub, Long userId) {
        return new OAuthSuccessEvent(username, email, provider, sub, userId);
    }
}
