package com.drinkhere.drinklymember.domain.auth.service;

import com.drinkhere.drinklymember.domain.auth.entity.OAuthMember;
import com.drinkhere.drinklymember.domain.auth.entity.OAuthOwner;
import com.drinkhere.drinklymember.domain.auth.repository.OAuthMemberRepository;
import com.drinkhere.drinklymember.domain.auth.repository.OAuthOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuthQueryService {

    private final OAuthMemberRepository oAuthMemberRepository;
    private final OAuthOwnerRepository oAuthOwnerRepository;

    public OAuthMember findMemberBySub(final String sub) {
        return oAuthMemberRepository.findBySub(sub).orElse(null);
    }

    public OAuthOwner findOwnerBySub(final String sub) {
        return oAuthOwnerRepository.findBySub(sub).orElse(null);
    }

    // 회원 등록 여부 확인 메소드 추가
    public boolean isRegistered(final String sub) {
        return oAuthMemberRepository.findBySub(sub)
                .map(OAuthMember::isRegistered)
                .orElse(false);  // OAuth 정보 없으면 기본값 false
    }
}
