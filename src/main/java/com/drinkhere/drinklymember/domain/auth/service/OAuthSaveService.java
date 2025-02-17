package com.drinkhere.drinklymember.domain.auth.service;

import com.drinkhere.drinklymember.domain.auth.entity.OAuthMember;
import com.drinkhere.drinklymember.domain.auth.repository.OAuthMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuthSaveService {

    private final OAuthMemberRepository oAuthRepository;

    public OAuthMember save(final OAuthMember oAuth) {
        oAuthRepository.save(oAuth);
        return oAuth;
    }
}
