package com.drinkhere.drinklymember.application.subscribe.presentation;

import com.drinkhere.drinklymember.common.response.ApplicationResponse;
import com.drinkhere.drinklymember.domain.member.service.member.MemberSubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/member/m")
@RequiredArgsConstructor
public class MemberSubscribeController {

    private final MemberSubscribeService memberSubscribeService;

    /**
     * 구독 상태 업데이트 API (구독 이력 ID 및 기간 반영)
     */
    @PostMapping("/subscribe-update")
    public ApplicationResponse<String> updateSubscriptionStatus(
            @RequestHeader("member-id") Long memberId,
            @RequestParam("subscription-history-id") Long subscriptionHistoryId,
            @RequestParam("duration-days") int durationDays) {

        memberSubscribeService.updateSubscriptionStatus(memberId, subscriptionHistoryId, durationDays);
        return ApplicationResponse.ok("구독 상태 업데이트 완료");
    }

    /**
     * 현재 사용자가 구독 중인지 확인하는 API (결제 전 검증)
     * - 결제 서비스에서 Feign으로 호출하여 구독 여부 확인
     */
    @GetMapping("/check-subscription")
    public ApplicationResponse<Boolean> isMemberSubscribed(@RequestHeader("member-id") Long memberId) {
        boolean isSubscribed = memberSubscribeService.isMemberSubscribed(memberId);
        return ApplicationResponse.ok(isSubscribed);
    }

    /**
     * 만료된 구독 목록 조회 API (배치 서버에서 호출)
     */
    @GetMapping("/expired-subscriptions")
    public ApplicationResponse<List<Long>> getExpiredSubscriptions() {
        List<Long> expiredSubscriptions = memberSubscribeService.getExpiredSubscriptions();
        return ApplicationResponse.ok(expiredSubscriptions);
    }

    /**
     * 구독 만료 처리 API (배치 서버에서 호출)
     */
    @PostMapping("/expire-subscriptions")
    public ApplicationResponse<String> expireSubscriptions(@RequestBody List<Long> expiredMemberIds) {
        memberSubscribeService.expireSubscriptions(expiredMemberIds);
        return ApplicationResponse.ok("구독 만료 상태 업데이트 완료");
    }
}
