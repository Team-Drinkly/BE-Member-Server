package com.drinkhere.drinklymember.domain.member.repository;

import com.drinkhere.drinklymember.common.exception.profile.ProfileException;
import com.drinkhere.drinklymember.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static com.drinkhere.drinklymember.common.exception.profile.ProfileErrorCode.MEMBER_NOT_FOUND;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByDi(String di);
    
    Optional<Member> findById(Long memberId);

    default Member findByIdOrThrow(Long memberId) {
        return findById(memberId)
                .orElseThrow(() -> new ProfileException(MEMBER_NOT_FOUND));
    }
}
