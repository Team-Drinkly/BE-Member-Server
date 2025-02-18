package com.drinkhere.drinklymember.domain.auth.service;

import com.drinkhere.drinklymember.common.annotation.DomainService;
import com.drinkhere.drinklymember.common.exception.oauth.AuthErrorCode;
import com.drinkhere.drinklymember.common.exception.oauth.OAuthNotFoundException;
import com.drinkhere.drinklymember.domain.auth.entity.OAuthOwner;
import com.drinkhere.drinklymember.domain.auth.repository.OAuthOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@Transactional
@RequiredArgsConstructor
public class OAuthUpdateService {

    private final OAuthOwnerRepository oAuthRepository;

    @Transactional
    public void updateRegisterStatus(Long id) {
        OAuthOwner oAuth = oAuthRepository.findById(id).orElseThrow(() -> new OAuthNotFoundException(AuthErrorCode.OAUTH_NOT_FOUND));
        oAuth.updateRegisterStatus(); // 가입 처리
    }
}
