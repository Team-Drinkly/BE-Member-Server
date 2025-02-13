package com.drinkhere.drinklymember.application.nice.service.impl;

import com.drinkhere.drinklymember.application.nice.service.MemberSignUpUseCase;
import com.drinkhere.drinklymember.common.annotation.ApplicationService;
import com.drinkhere.drinklymember.domain.auth.service.OAuthUpdateService;
import com.drinkhere.drinklymember.domain.member.dto.MemberSignUpRequest;
import com.drinkhere.drinklymember.domain.member.service.MemberCommandService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class MemberSignUpUseCaseImpl implements MemberSignUpUseCase {
    private final OAuthUpdateService oAuthUpdateService;
    private final MemberCommandService memberCommandService;

    @Override
    public void signUp(MemberSignUpRequest memberSignUpRequest) {
        // 멤버 정보 저장
        memberCommandService.save(memberSignUpRequest.toEntity());

        // oauth 테이블 업데이트 -> register 상태로
        oAuthUpdateService.updateRegisterStatus(memberSignUpRequest.memberId());
    }
}
