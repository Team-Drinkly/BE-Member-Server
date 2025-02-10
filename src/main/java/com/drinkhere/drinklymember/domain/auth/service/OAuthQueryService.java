package com.drinkhere.drinklymember.domain.auth.service;

import com.drinkhere.drinklymember.common.exception.oauth.AuthErrorCode;
import com.drinkhere.drinklymember.common.exception.oauth.OAuthNotFoundException;
import com.drinkhere.drinklymember.domain.auth.entity.OAuth;
import com.drinkhere.drinklymember.domain.auth.repository.OAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthQueryService {

    private final OAuthRepository oAuthRepository;

    public boolean existBySub(final String sub) {
        return oAuthRepository.existsBySub(sub);
    }

    public OAuth findBySub(final String sub) {
        return oAuthRepository.findBySub(sub)
                .orElseThrow(() -> new OAuthNotFoundException(AuthErrorCode.OAUTH_NOT_FOUND));
    }
}
