package com.drinkhere.drinklymember.domain.auth.service;

import com.drinkhere.drinklymember.domain.auth.entity.OAuthMember;
import com.drinkhere.drinklymember.domain.auth.repository.OAuthMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuthQueryService {

    private final OAuthMemberRepository oAuthRepository;

    public OAuthMember findBySub(final String sub) {
        return oAuthRepository.findBySub(sub).orElse(null);
    }

    // 회원 등록 여부 확인 메소드 추가
    public boolean isRegistered(final String sub) {
        return oAuthRepository.findBySub(sub)
                .map(OAuthMember::isRegistered)
                .orElse(false);  // OAuth 정보 없으면 기본값 false
    }
}
