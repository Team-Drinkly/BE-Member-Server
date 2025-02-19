package com.drinkhere.drinklymember.domain.member.service.member;

import com.drinkhere.drinklymember.domain.member.entity.MemberSubscribe;
import com.drinkhere.drinklymember.domain.member.repository.MemberSubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
}
