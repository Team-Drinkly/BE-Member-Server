package com.drinkhere.drinklymember.domain.auth.dto;

public record OAuthResponse(
        Long oauthId,
        Boolean isRegistered) {
}
