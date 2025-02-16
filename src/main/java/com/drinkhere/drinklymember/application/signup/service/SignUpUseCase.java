package com.drinkhere.drinklymember.application.signup.service;

import com.drinkhere.drinklymember.domain.member.dto.MemberSignUpRequest;

public interface SignUpUseCase<T> {
    void signUp(T request);
}