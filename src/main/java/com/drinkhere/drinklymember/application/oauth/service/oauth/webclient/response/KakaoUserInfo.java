package com.drinkhere.drinklymember.application.oauth.service.oauth.webclient.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoUserInfo {

    @JsonAlias("id")
    private String sub;

    @JsonAlias("kakao_account.email")
    private String email;

    @JsonAlias("kakao_account.profile.nickname")
    private String nickname;

    @JsonAlias("kakao_account.profile.profile_image")
    private String picture;

}
