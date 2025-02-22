package com.drinkhere.drinklymember.application.signup.service.impl;

import com.drinkhere.drinklymember.application.signup.service.SignUpUseCase;
import com.drinkhere.drinklymember.common.annotation.ApplicationService;
import com.drinkhere.drinklymember.domain.auth.dto.Token;
import com.drinkhere.drinklymember.domain.auth.jwt.JWTProvider;
import com.drinkhere.drinklymember.domain.auth.service.OAuthUpdateService;
import com.drinkhere.drinklymember.domain.member.dto.signup.OwnerSignUpRequest;
import com.drinkhere.drinklymember.domain.member.service.owner.OwnerCommandService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class OwnerSignUpUseCaseImpl implements SignUpUseCase<OwnerSignUpRequest> {

    private final OAuthUpdateService oAuthUpdateService;
    private final OwnerCommandService ownerCommandService;
    private final JWTProvider jwtProvider;

    @Override
    public Token signUp(OwnerSignUpRequest request) {

        // 사장님 정보 저장
        ownerCommandService.save(request.toOwnerEntity());

        // OAuth 등록 상태 변경
        oAuthUpdateService.updateOwnerRegisterStatus(request.ownerId());

        // JWT 생성 및 반환
        return jwtProvider.generateOwnerToken(request.ownerId());
    }
}
