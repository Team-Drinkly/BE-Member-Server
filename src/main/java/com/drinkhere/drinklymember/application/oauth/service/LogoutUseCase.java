package com.drinkhere.drinklymember.application.oauth.service;

public interface LogoutUseCase {

    void logoutAccessUser(String refreshTokenHeader);
}
