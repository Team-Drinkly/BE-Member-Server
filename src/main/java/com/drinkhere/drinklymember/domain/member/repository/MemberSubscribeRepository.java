package com.drinkhere.drinklymember.domain.member.repository;

import com.drinkhere.drinklymember.common.exception.profile.ProfileException;
import com.drinkhere.drinklymember.domain.member.entity.MemberSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static com.drinkhere.drinklymember.common.exception.profile.ProfileErrorCode.MEMBER_SUBSCRIBE_NOT_FOUND;

public interface MemberSubscribeRepository extends JpaRepository<MemberSubscribe, Long> {

    /**
     * 멤버 ID와 구독 상태를 기준으로 구독 정보 조회
     */
    Optional<MemberSubscribe> findByMemberIdAndIsSubscribed(Long memberId, Boolean isSubscribed);

    /**
     * 멤버가 구독 상태인지 확인
     */
    boolean existsByMemberIdAndIsSubscribed(Long memberId, Boolean isSubscribed);

    Optional<MemberSubscribe> findByMemberId(Long memberId);

    default MemberSubscribe findByMemberIdOrThrow(Long memberId) {
        return findByMemberId(memberId)
                .orElseThrow(() -> new ProfileException(MEMBER_SUBSCRIBE_NOT_FOUND));
    }
}
