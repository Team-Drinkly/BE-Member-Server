package com.drinkhere.drinklymember.domain.auth.service;

import com.drinkhere.drinklymember.common.annotation.DomainService;
import com.drinkhere.drinklymember.common.exception.oauth.AuthErrorCode;
import com.drinkhere.drinklymember.common.exception.oauth.OAuthNotFoundException;
import com.drinkhere.drinklymember.domain.auth.entity.OAuth;
import com.drinkhere.drinklymember.domain.auth.repository.OAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@Transactional
@RequiredArgsConstructor
public class OAuthUpdateService {
    private final OAuthRepository oAuthRepository;

    @Transactional
    public void updateRegisterStatus(Long id) {
        OAuth oAuth = oAuthRepository.findById(id).orElseThrow(() -> new OAuthNotFoundException(AuthErrorCode.OAUTH_NOT_FOUND));
        oAuth.updateRegisterStatus(); // 가입 처리
    }
}
