package com.drinkhere.drinklymember.domain.auth.service;

import com.drinkhere.drinklymember.domain.auth.entity.OAuthMember;
import com.drinkhere.drinklymember.domain.auth.entity.OAuthOwner;
import com.drinkhere.drinklymember.domain.auth.repository.OAuthMemberRepository;
import com.drinkhere.drinklymember.domain.auth.repository.OAuthOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuthSaveService {

    private final OAuthMemberRepository oAuthMemberRepository;
    private final OAuthOwnerRepository oAuthOwnerRepository;

    /**
     * 멤버 OAuth 저장
     */
    public OAuthMember memberSave(final OAuthMember oAuthMember) {
        return saveOAuth(oAuthMemberRepository, oAuthMember);
    }

    /**
     * 사장님 OAuth 저장
     */
    public OAuthOwner ownerSave(final OAuthOwner oAuthOwner) {
        return saveOAuth(oAuthOwnerRepository, oAuthOwner);
    }

    /**
     * 공통 OAuth 저장 메서드
     */
    private <T> T saveOAuth(final JpaRepository<T, Long> repository, final T entity) {
        return repository.save(entity);
    }
}
