package com.drinkhere.drinklymember.domain.member.repository;

import com.drinkhere.drinklymember.common.exception.profile.ProfileException;
import com.drinkhere.drinklymember.domain.member.entity.MemberSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
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

    /**
     * 만료된 구독을 조회 (현재 시간이 expireDate를 지난 경우)
     */
    @Query("SELECT m.memberId FROM MemberSubscribe m WHERE m.isSubscribed = true AND m.expireDate <= :now")
    List<Long> findExpiredSubscriptions(LocalDateTime now);

    /**
     * 만료된 구독 상태를 업데이트 (isSubscribed = false)
     */
    @Modifying
    @Query("UPDATE MemberSubscribe m SET m.isSubscribed = false WHERE m.memberId IN :expiredMemberIds")
    void updateExpiredSubscriptions(List<Long> expiredMemberIds);
}
