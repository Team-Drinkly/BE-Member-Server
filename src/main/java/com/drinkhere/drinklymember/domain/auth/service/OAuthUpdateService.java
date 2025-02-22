package com.drinkhere.drinklymember.domain.auth.service;

import com.drinkhere.drinklymember.common.annotation.DomainService;
import com.drinkhere.drinklymember.common.exception.oauth.AuthErrorCode;
import com.drinkhere.drinklymember.common.exception.oauth.OAuthNotFoundException;
import com.drinkhere.drinklymember.domain.auth.entity.OAuthMember;
import com.drinkhere.drinklymember.domain.auth.entity.OAuthOwner;
import com.drinkhere.drinklymember.domain.auth.repository.OAuthMemberRepository;
import com.drinkhere.drinklymember.domain.auth.repository.OAuthOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@Transactional
@RequiredArgsConstructor
public class OAuthUpdateService {
    private final OAuthMemberRepository oauthMemberRepository;
    private final OAuthOwnerRepository oAuthOwnerRepository;

    @Transactional
    public void updateMemberRegisterStatus(Long id) {
        OAuthMember oAuthMember = oauthMemberRepository.findById(id).orElseThrow(() -> new OAuthNotFoundException(AuthErrorCode.OAUTH_NOT_FOUND));
        oAuthMember.updateRegisterStatus(); // 가입 처리
    }

    @Transactional
    public void updateOwnerRegisterStatus(Long id) {
        OAuthOwner oAuthOwner = oAuthOwnerRepository.findById(id).orElseThrow(() -> new OAuthNotFoundException(AuthErrorCode.OAUTH_NOT_FOUND));
        oAuthOwner.updateRegisterStatus(); // 가입 처리
    }
}
