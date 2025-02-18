package com.drinkhere.drinklymember.application.signup.service.impl;

import com.drinkhere.drinklymember.application.signup.service.SignUpUseCase;
import com.drinkhere.drinklymember.common.annotation.ApplicationService;
import com.drinkhere.drinklymember.domain.auth.dto.Token;
import com.drinkhere.drinklymember.domain.auth.jwt.JWTProvider;
import com.drinkhere.drinklymember.domain.auth.service.OAuthUpdateService;
import com.drinkhere.drinklymember.domain.member.dto.MemberSignUpRequest;
import com.drinkhere.drinklymember.domain.member.service.member.MemberCommandService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class MemberSignUpUseCaseImpl implements SignUpUseCase<MemberSignUpRequest> {

    private final OAuthUpdateService oAuthUpdateService;
    private final MemberCommandService memberCommandService;
    private final JWTProvider jwtProvider;

    @Override
    public Token signUp(MemberSignUpRequest memberSignUpRequest) {
        // 멤버 정보 저장
        memberCommandService.save(memberSignUpRequest.toEntity());

        // OAuth 등록 상태 변경
        oAuthUpdateService.updateMemberRegisterStatus(memberSignUpRequest.memberId());

        // JWT 생성 및 반환
        return jwtProvider.generateMemberToken(memberSignUpRequest.memberId());
    }
}
