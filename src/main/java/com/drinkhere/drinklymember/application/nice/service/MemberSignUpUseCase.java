package com.drinkhere.drinklymember.application.nice.service;

import com.drinkhere.drinklymember.domain.member.dto.MemberSignUpRequest;

public interface MemberSignUpUseCase {
    void signUp(MemberSignUpRequest memberSignUpRequest);
}
