package com.drinkhere.drinklymember.application.subscribe.presentation;

import com.drinkhere.drinklymember.application.subscribe.presentation.docs.MemberSubscribeControllerDocs;
import com.drinkhere.drinklymember.common.response.ApplicationResponse;
import com.drinkhere.drinklymember.domain.member.service.member.MemberSubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member/m")
@RequiredArgsConstructor
public class MemberSubscribeController implements MemberSubscribeControllerDocs {

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
}
