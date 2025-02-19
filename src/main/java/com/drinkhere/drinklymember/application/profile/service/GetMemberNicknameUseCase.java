package com.drinkhere.drinklymember.application.profile.service;

import com.drinkhere.drinklymember.common.annotation.ApplicationService;
import com.drinkhere.drinklymember.domain.member.dto.profile.response.MemberResponse;
import com.drinkhere.drinklymember.domain.member.entity.Member;
import com.drinkhere.drinklymember.domain.member.service.member.MemberQueryService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@ApplicationService
@RequiredArgsConstructor
public class GetMemberNicknameUseCase {
    private final MemberQueryService memberQueryService;

    public MemberResponse getMemberNickname(Long memberId) {
        return new MemberResponse(memberQueryService.findById(memberId).getNickname());
    }
}
