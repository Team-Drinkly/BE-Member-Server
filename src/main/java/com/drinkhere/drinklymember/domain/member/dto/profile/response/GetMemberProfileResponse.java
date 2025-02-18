package com.drinkhere.drinklymember.domain.member.dto.profile.response;

import com.drinkhere.drinklymember.domain.member.entity.Member;

public record GetMemberProfileResponse(
        Long memberId,
        String nickname,
        boolean isSubscribe,
        SubscribeInfo subscribeInfo

) {
    public static GetMemberProfileResponse toDto(Member member, boolean isSubscribe, SubscribeInfo subscribeInfo) {
        return new GetMemberProfileResponse(
                member.getId(),
                member.getNickname(),
                isSubscribe,
                subscribeInfo
        );
    }
}
