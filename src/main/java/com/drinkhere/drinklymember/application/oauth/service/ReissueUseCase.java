package com.drinkhere.drinklymember.application.oauth.service;

import com.drinkhere.drinklymember.domain.auth.dto.TokenReissueResponse;

public interface ReissueUseCase {

    TokenReissueResponse reissue(String refreshToken);
}
