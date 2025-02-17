package com.drinkhere.drinklymember.application.oauth.service;

import com.drinkhere.drinklymember.domain.auth.dto.OAuthResponse;
import com.drinkhere.drinklymember.domain.auth.enums.Authority;
import com.drinkhere.drinklymember.domain.auth.enums.Provider;

public interface OAuthUseCase {

    OAuthResponse oAuthLogin(Authority authority, Provider provider, String accessToken);

}
