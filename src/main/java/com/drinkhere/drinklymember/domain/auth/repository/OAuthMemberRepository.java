package com.drinkhere.drinklymember.domain.auth.repository;

import com.drinkhere.drinklymember.domain.auth.entity.OAuthMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthMemberRepository extends JpaRepository<OAuthMember, Long> {

    boolean existsBySub(String sub);

    Optional<OAuthMember> findBySub(String sub);

    Optional<OAuthMember> findById(Long id);
}
