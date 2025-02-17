package com.drinkhere.drinklymember.application.signup.service;

import com.drinkhere.drinklymember.domain.auth.dto.Token;

public interface SignUpUseCase<T> {

    Token signUp(T request);
}