package com.drinkhere.drinklymember.application.oauth.service;

import com.drinkhere.drinklymember.domain.auth.dto.OAuthResponse;
import com.drinkhere.drinklymember.domain.auth.enums.Provider;

public interface OAuthUseCase {

    OAuthResponse oAuthLogin(Provider provider, String accessToken);

}
