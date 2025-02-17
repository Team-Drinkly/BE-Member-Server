package com.drinkhere.drinklymember.domain.auth.service;

import com.drinkhere.drinklymember.common.exception.oauth.AuthErrorCode;
import com.drinkhere.drinklymember.common.exception.oauth.OAuthNotFoundException;
import com.drinkhere.drinklymember.domain.auth.entity.OAuth;
import com.drinkhere.drinklymember.domain.auth.repository.OAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuthQueryService {

    private final OAuthRepository oAuthRepository;

    public OAuth findBySub(final String sub) {
        return oAuthRepository.findBySub(sub)
                .orElseThrow(() -> new OAuthNotFoundException(AuthErrorCode.OAUTH_NOT_FOUND));
    }

    // 회원 등록 여부 확인 메소드 추가
    public boolean isRegistered(final String sub) {
        return oAuthRepository.findBySub(sub)
                .map(OAuth::isRegistered)
                .orElse(false);  // OAuth 정보 없으면 기본값 false
    }
}
