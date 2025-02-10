package com.drinkhere.drinklymember.application.oauth.service.impl;

import com.drinkhere.drinklymember.application.oauth.service.OAuthUseCase;
import com.drinkhere.drinklymember.application.oauth.service.oauth.OAuthInvoker;
import com.drinkhere.drinklymember.common.annotation.ApplicationService;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthRequest;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthResponse;
import com.drinkhere.drinklymember.domain.auth.enums.Provider;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class OAuthUseCaseImpl implements OAuthUseCase {

    private final OAuthInvoker oAuthInvoker;

    @Override
    public OAuthResponse oAuthLogin(Provider provider, String accessToken) {
        return oAuthInvoker.execute(new OAuthRequest(provider, accessToken));
    }
}
