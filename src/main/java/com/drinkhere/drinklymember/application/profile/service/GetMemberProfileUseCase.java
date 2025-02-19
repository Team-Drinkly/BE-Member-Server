package com.drinkhere.drinklymember.application.profile.service;

import com.drinkhere.drinklymember.common.annotation.ApplicationService;
import com.drinkhere.drinklymember.domain.member.dto.profile.response.GetMemberProfileResponse;
import com.drinkhere.drinklymember.domain.member.dto.profile.response.SubscribeInfo;
import com.drinkhere.drinklymember.domain.member.entity.Member;
import com.drinkhere.drinklymember.domain.member.entity.MemberSubscribe;
import com.drinkhere.drinklymember.domain.member.service.member.MemberQueryService;
import com.drinkhere.drinklymember.domain.member.service.member.MemberSubscribeQueryService;
import com.drinkhere.drinklymember.openfeign.client.StoreClient;
import com.drinkhere.drinklymember.openfeign.dto.response.CountFreeDrinkHistories;
import com.drinkhere.drinklymember.util.TimeUtil;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationService
@RequiredArgsConstructor
public class GetMemberProfileUseCase {
    private final MemberQueryService memberQueryService;
    private final MemberSubscribeQueryService memberSubscribeQueryService;
    private final StoreClient storeClient;
    public GetMemberProfileResponse getMemberProfile(Long memberId) {
        Member member = memberQueryService.findById(memberId);
        Optional<MemberSubscribe> memberSubscribeOpt = memberSubscribeQueryService.getMemberSubscribe(memberId);
        if (memberSubscribeOpt.isEmpty()) {
            return GetMemberProfileResponse.toDto(member, false, null);
        }

        MemberSubscribe memberSubscribe = memberSubscribeOpt.get();

        // 외부 API 호출 (구독 ID가 필요)
        CountFreeDrinkHistories countResponse = storeClient.getCountFreeDrinkHistoriesBySubscribeId(
                memberId, memberSubscribe.getSubscribeId()
        );

        int leftDays = (int) Duration.between(LocalDateTime.now(), memberSubscribe.getExpireDate()).toDays();

        String expiredDate = TimeUtil.refineToDate(memberSubscribe.getExpireDate());

        SubscribeInfo subscribeInfo = new SubscribeInfo(leftDays, expiredDate, countResponse.getCount(), countResponse.getPayload());

        return GetMemberProfileResponse.toDto(member, true, subscribeInfo);
    }
}
