package com.drinkhere.drinklymember.domain.member.service.member;

import com.drinkhere.drinklymember.domain.member.entity.MemberSubscribe;
import com.drinkhere.drinklymember.domain.member.repository.MemberSubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberSubscribeService {

    private final MemberSubscribeRepository memberSubscribeRepository;

    /**
     * 구독 상태 업데이트 (구독 기간 반영)
     */
    @Transactional
    public void updateSubscriptionStatus(Long memberId, Long subscriptionHistoryId, int durationDays) {

        // durationDays 값을 받아서 만료일 계산
        LocalDateTime expireDate = LocalDateTime.now().plusDays(durationDays);

        MemberSubscribe subscription = MemberSubscribe.builder()
                .memberId(memberId)
                .subscribeId(subscriptionHistoryId) // 구독 이력 ID 저장
                .isSubscribed(true)
                .expireDate(expireDate) // 전달받은 durationDays를 이용해 만료일 계산
                .build();

        memberSubscribeRepository.save(subscription);
    }

    /**
     * 현재 사용자가 구독 중인지 확인 (결제 전 검증)
     */
    @Transactional(readOnly = true)
    public boolean isMemberSubscribed(Long memberId) {
        return memberSubscribeRepository.existsByMemberIdAndIsSubscribed(memberId, true);
    }

    /**
     * 만료된 구독 목록 조회 (배치 서버에서 호출)
     */
    @Transactional(readOnly = true)
    public List<Long> getExpiredSubscriptions() {
        return memberSubscribeRepository.findExpiredSubscriptions(LocalDateTime.now());
    }

    /**
     * 구독 만료 처리 (배치 서버에서 호출)
     */
    @Transactional
    public void expireSubscriptions(List<Long> expiredMemberIds) {
        memberSubscribeRepository.updateExpiredSubscriptions(expiredMemberIds);
    }

}
