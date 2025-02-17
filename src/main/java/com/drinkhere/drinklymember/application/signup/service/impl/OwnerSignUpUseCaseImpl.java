package com.drinkhere.drinklymember.application.signup.service.impl;

import com.drinkhere.drinklymember.application.signup.service.SignUpUseCase;
import com.drinkhere.drinklymember.common.annotation.ApplicationService;
import com.drinkhere.drinklymember.domain.auth.service.OAuthUpdateService;
import com.drinkhere.drinklymember.domain.member.dto.OwnerSignUpRequest;
import com.drinkhere.drinklymember.domain.member.service.owner.OwnerCommandService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class OwnerSignUpUseCaseImpl implements SignUpUseCase<OwnerSignUpRequest> {
    private final OAuthUpdateService oAuthUpdateService;
    private final OwnerCommandService ownerCommandService;
//    private final StoreEventProducer storeEventProducer;

    @Override
    public void signUp(OwnerSignUpRequest request) {

        // 사장님 정보 저장
        ownerCommandService.save(request.toOwnerEntity());

        // oauth 테이블 업데이트 -> register 상태로
        oAuthUpdateService.updateRegisterStatus(request.ownerId());
    }
}
