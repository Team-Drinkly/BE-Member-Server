package com.drinkhere.drinklymember.application.signup.service;

public interface SignUpUseCase<T> {
    void signUp(T request);
}